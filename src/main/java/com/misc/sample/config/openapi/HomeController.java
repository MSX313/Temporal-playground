package com.misc.sample.config.openapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springdoc.core.utils.Constants.SWAGGER_UI_PATH;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

@Controller
public class HomeController {

    @Value(SWAGGER_UI_PATH)
    private String swaggerUiPath;

    @SuppressWarnings("all")
    @RequestMapping(DEFAULT_PATH_SEPARATOR)
    public String home() {
        return REDIRECT_URL_PREFIX + swaggerUiPath;
    }
}


