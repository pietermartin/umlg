package org.umlg.runtime.adaptor

/**
 * Created by pieter on 2014/04/21.
 */
class GremlinGroovyPipelineInterceptor implements Interceptor {
    @Override
    Object beforeInvoke(Object object, String methodName, Object[] arguments) {
        null
    }

    @Override
    Object afterInvoke(Object object, String methodName, Object[] arguments, Object result) {
        if( methodName == 'V' ) {
//            GremlinGroovyPipeline pipeline = (GremlinGroovyPipeline)result;
            return result.hasNot('deleted');
        } else {
            return result
        }
    }

    @Override
    boolean doInvoke() {
        return true
    }

}
