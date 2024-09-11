package com.yupi.openapigateway;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import com.yess.openapiclientsdk.utils.SignUtils;
import com.yupi.openapicommon.model.entity.InterfaceInfo;
import com.yupi.openapicommon.model.entity.User;
import com.yupi.openapicommon.service.InnerInterfaceInfoService;
import com.yupi.openapicommon.service.InnerUserInterfaceInfoService;
import com.yupi.openapicommon.service.InnerUserService;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 全局过滤
 * @author yess
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @DubboReference
    private InnerUserService innerUserService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8123";

//         * 1. 用户请求网关，调用接口
//     * 2. 统一请求日志
//     * 3. 统一判断请求参数是否合法
//     * 4. 统一鉴权ak、sk
//     * 5. 接口是否存在
//     * 3. 路由到具体接口地址，调用接口
//     * 6. 返回调用结果，接口调用次数 + 1
//            * 7. 响应日志

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("custom global filter");
        // 1. 统一请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求的唯一标识：" + request.getId());
        log.info("请求来源地址：" + request.getRemoteAddress());
//        log.info("请求来源地址：" + request.getLocalAddress().getHostString());
        log.info("请求路径：" + request.getPath());
        log.info("请求方法：" + request.getMethodValue());
        log.info("请求参数：" + request.getQueryParams());

        // 2. 设置白名单
        ServerHttpResponse response = exchange.getResponse();
        if(!IP_WHITE_LIST.contains(request.getLocalAddress().getHostString())){
            return handleNoAuth(response);
        }

        // 3. 鉴权ak、sk
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
//        String secreteKey = request.getHeader("secreteKey");
        String body = headers.getFirst("body");
        String nounce = headers.getFirst("nounce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");

        if(StringUtils.isEmpty(accessKey) || StringUtils.isEmpty(nounce) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(sign)){
            return handleNoAuth(response);
        }

        // 时间不超过五分钟（不经过数据库，先进行校验筛选）
        final long FIVE_MINITES = 5 * 60;
        long currentTime = System.currentTimeMillis() / 1000;
        long requestTime = Long.parseLong(timestamp) / 1000;
        if(currentTime - requestTime > FIVE_MINITES){
            return handleNoAuth(response);
        }

        // 从数据库读取accessKey信息校验
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getUserInfo error", e);
        }
        if(invokeUser == null){
            return handleNoAuth(response);
        }

        // todo 校验随机数，检查数据库中是否重复，存入数据库
        long nounceL = Long.parseLong(nounce);
        if(nounceL < 0 || nounceL >= 10000L){
            return handleNoAuth(response);
        }

        // 从数据库中读取secreteKey做校验
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("accessKey", accessKey);
        headerMap.put("body", body);
        headerMap.put("nounce", nounce);
        headerMap.put("timestamp", timestamp);

        String serverSign = SignUtils.getSign(headerMap, invokeUser.getSecreteKey());
        if(!serverSign.equals(sign)){
            return handleNoAuth(response);
        }

        // 4. 请求接口是否存在
        // 从数据库中查询模拟接口是否存在，以及请求方法是否匹配（还可以校验请求参数）
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(INTERFACE_HOST, String.valueOf(request.getPath()), request.getMethodValue());
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if(interfaceInfo == null){
            return handleNoAuth(response);
        }

        // 校验是否还有调用次数


//        // 5. 转发，调用接口
//        Mono<Void> filter = chain.filter(exchange);
//        // 6. 响应日志
//        log.info("响应状态码：" + response.getStatusCode());
//        // 7. 接口调用次数 + 1
//        // todo 调用invokeCount方法，接口调用次数 + 1
//        // 8. 统一处理响应错误
//        if(response.getStatusCode() != HttpStatus.OK){
//            return handleInvokeError(response);
//        }

        return handleResponse(exchange, chain, invokeUser.getId(), interfaceInfo.getId());

    }

    /**
     * 返回值处理
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long userId, Long interfaceInfoId) {
        try {
            ServerHttpResponse response = exchange.getResponse();
            DataBufferFactory bufferFactory = response.bufferFactory();

            ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(response) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        // 获取响应类型，如果是 json 就打印
                        String originalResponseContentType = exchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                        if (ObjectUtil.equal(this.getStatusCode(), HttpStatus.OK)
                                && !StringUtils.isEmpty(originalResponseContentType)
                                && (originalResponseContentType.contains("application/json") || originalResponseContentType.contains("text/html"))) {

                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                                // 合并多个流集合，解决返回体分段传输
                                DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                                DataBuffer join = dataBufferFactory.join(dataBuffers);
                                byte[] content = new byte[join.readableByteCount()];
                                join.read(content);

                                // 释放掉内存
                                DataBufferUtils.release(join);
                                String responseResult = new String(content, StandardCharsets.UTF_8);

                                log.info("响应结果：" + responseResult);

                                // 7. 接口调用次数 + 1
                                // 调用invokeCount方法，接口调用次数 + 1
                                boolean invokeCountResult = false;
                                try {
                                    invokeCountResult = innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                } catch (Exception e) {
                                    log.error("invokeCount error", e);
                                }

                                return bufferFactory.wrap(content);
                            }));
                        }else{
                            log.error("响应code异常----", this.getStatusCode());
                            return chain.filter(exchange);
                        }
                    }
                    // if body is not a flux. never got there.
                    return super.writeWith(body);
                }
            };
            // replace response with decorator
            return chain.filter(exchange.mutate().response(responseDecorator).build());
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    private Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

