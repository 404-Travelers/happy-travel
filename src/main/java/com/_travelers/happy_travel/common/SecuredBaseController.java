package com._travelers.happy_travel.common;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("isAuthenticated()")
public abstract class SecuredBaseController {

}
