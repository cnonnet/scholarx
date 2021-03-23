package org.sefglobal.scholarx.controller;

import java.util.List;
import org.sefglobal.scholarx.exception.BadRequestException;
import org.sefglobal.scholarx.exception.NoContentException;
import org.sefglobal.scholarx.exception.ResourceNotFoundException;
import org.sefglobal.scholarx.exception.UnauthorizedException;
import org.sefglobal.scholarx.model.Mentee;
import org.sefglobal.scholarx.model.Profile;
import org.sefglobal.scholarx.model.Program;
import org.sefglobal.scholarx.service.IntrospectionService;
import org.sefglobal.scholarx.util.EnrolmentState;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
public class AuthUserController {

  private final IntrospectionService introspectionService;

  public AuthUserController(IntrospectionService introspectionService) {
    this.introspectionService = introspectionService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Object getLoggedUser(Authentication authentication)
    throws ResourceNotFoundException, UnauthorizedException {
    return authentication.getPrincipal();
  }

  @GetMapping("/programs/mentee")
  @ResponseStatus(HttpStatus.OK)
  public List<Program> getMenteeingPrograms(Authentication authentication)
    throws ResourceNotFoundException, NoContentException {
    Profile profile = (Profile) authentication.getPrincipal();
    return introspectionService.getMenteeingPrograms(profile.getId());
  }

  @GetMapping("/programs/mentor")
  @ResponseStatus(HttpStatus.OK)
  public List<Program> getMentoringPrograms(Authentication authentication)
    throws ResourceNotFoundException, NoContentException {
    Profile profile = (Profile) authentication.getPrincipal();
    return introspectionService.getMentoringPrograms(profile.getId());
  }

  @GetMapping("/programs/{id}/mentees")
  @ResponseStatus(HttpStatus.OK)
  public List<Mentee> getMentees(
    Authentication authentication,
    @PathVariable long id,
    @RequestParam(required = false) List<EnrolmentState> menteeStates
  )
    throws ResourceNotFoundException, NoContentException {
    Profile profile = (Profile) authentication.getPrincipal();
    return introspectionService.getMentees(id, profile.getId(), menteeStates);
  }

  @PutMapping("/mentor/{id}/confirmation")
  @ResponseStatus(HttpStatus.OK)
  public Mentee confirmMentor(
    @PathVariable long id,
    Authentication authentication
  )
    throws ResourceNotFoundException, BadRequestException {
    Profile profile = (Profile) authentication.getPrincipal();
    return introspectionService.confirmMentor(id, profile.getId());
  }
}
