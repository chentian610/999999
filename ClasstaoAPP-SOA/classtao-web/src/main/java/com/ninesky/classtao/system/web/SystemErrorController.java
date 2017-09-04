package com.ninesky.classtao.system.web;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;

@Controller
public class SystemErrorController implements ErrorController {
    private static Logger logger = LoggerFactory.getLogger(SystemErrorController.class);
    private static final String ERROR_PATH = "/error";
    /**
     * Error Attributes in the Application
     */
    @Autowired
    private ErrorAttributes errorAttributes;


    @GetMapping(value=ERROR_PATH)
    public ModelAndView handleError(HttpServletRequest request){
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Map<String, Object> map = this.errorAttributes.getErrorAttributes(requestAttributes,false);
        String URL = request.getRequestURL().toString();
        map.put("URL", URL);
        logger.info("AppErrorController.method [error info]: status-" + map.get("status") +", request url-" + URL);
        switch ((int) map.get("status")) {
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                return new ModelAndView("error_500", map);
            case SC_NOT_FOUND:
                return new ModelAndView("error_404", map);
            default:
                return new ModelAndView("error_500", map);
        }
    }

    @Override
    public String getErrorPath() {
        // TODO Auto-generated method stub
        return ERROR_PATH;
    }

}
