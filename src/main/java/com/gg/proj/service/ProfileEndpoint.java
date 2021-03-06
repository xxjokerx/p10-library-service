package com.gg.proj.service;

import com.gg.proj.business.ProfileManager;
import com.gg.proj.service.exceptions.GenericExceptionHelper;
import com.gg.proj.service.exceptions.ServiceFaultException;
import com.gg.proj.service.profiles.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * <p>This class is registered with Spring WS as a candidate for processing incoming SOAP messages (via Endpoint annotation).</p>
 *
 * <p>Other annotations you'll find in this class :</p>
 *
 * <p>PayloadRoot is then used by Spring WS to pick the handler method based on the message’s namespace and localPart.</p>
 * <p>RequestPayload indicates that the incoming message will be mapped to the method’s request parameter.</p>
 * <p>ResponsePayload annotation makes Spring WS map the returned value to the response payload.</p>
 *
 * <p>UserEndpoint and ProfileEndpoint are both user control's classes but they are split in two separated class. </p>
 * <p>ProfileEndpoint regroups most CRUD methods excepted 'create'.</p>
 */
@Endpoint
public class ProfileEndpoint {

    private static final Logger log = LoggerFactory.getLogger(ProfileEndpoint.class);

    private static final String NAMESPACE_URI = "http://proj.gg.com/service/profiles";

    private ProfileManager profileManager;

    @Autowired
    public ProfileEndpoint(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    /**
     * <p>This methods is exposed. It uses the RequestPayload to do a custom call to the Business layer.</p>
     *
     * <p>There is a verification on token UUID (the user must must possess a valid to perform this
     * method).</p>
     *
     * <p>Exceptions thrown by the Business layer (InvalidTokenException, OutdatedTokenException) are processed by the
     * serviceFaultExceptionHandler : depending the instance of the exception it builds a custom SOAP error.</p>
     *
     * @param request is an instance of SaveProfileRequest. It's mapped from the incoming SOAP message.
     * @return SaveProfileResponse the output message contains this response.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "saveProfileRequest")
    @ResponsePayload
    public SaveProfileResponse saveProfile(@RequestPayload SaveProfileRequest request) throws ServiceFaultException {
        log.info("Call from network - saveProfile for user : [" + request.getUser().toString() + "]");
        SaveProfileResponse saveProfileResponse = new SaveProfileResponse();
        try {
            Optional<User> optional = profileManager.save(request.getUser(), request.getTokenUUID());
            optional.ifPresent(saveProfileResponse::setUser);
        } catch (Exception ex) {
            GenericExceptionHelper.serviceFaultExceptionHandler(ex);
        }
        return saveProfileResponse;
    }

    /**
     * <p>This methods is exposed. It uses the RequestPayload to do a custom call to the Business layer.</p>
     *
     * <p>There is a verification on token UUID (the user must must possess a valid to perform this
     * method).</p>
     *
     * <p>Exceptions thrown by the Business layer (InvalidTokenException, OutdatedTokenException) are processed by the
     * serviceFaultExceptionHandler : depending the instance of the exception it builds a custom SOAP error.</p>
     *
     * @param request is an instance of DeleteProfileRequest. It's mapped from the incoming SOAP message.
     * @return DeleteProfileResponse the output message contains this response.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteProfileRequest")
    @ResponsePayload
    public DeleteProfileResponse deleteProfile(@RequestPayload DeleteProfileRequest request) throws ServiceFaultException {
        log.info("Call from network - deleteProfile for user : [" + request.getUser().getPseudo() + "]");
        try {
            profileManager.delete(request.getUser(), request.getTokenUUID());
        } catch (Exception ex) {
            GenericExceptionHelper.serviceFaultExceptionHandler(ex);
        }
        return new DeleteProfileResponse();
    }

    /**
     * <p>This methods is exposed. It uses the RequestPayload to do a custom call to the Business layer.</p>
     *
     * <p>There is a verification on token UUID (the user must must possess a valid to perform this
     * method).</p>
     *
     * <p>Exceptions thrown by the Business layer (InvalidTokenException, OutdatedTokenException) are processed by the
     * serviceFaultExceptionHandler : depending the instance of the exception it builds a custom SOAP error.</p>
     *
     * @param request is an instance of GetProfileRequest. It's mapped from the incoming SOAP message.
     * @return GetProfileResponse the output message contains this response.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProfileRequest")
    @ResponsePayload
    public GetProfileResponse getProfile(@RequestPayload GetProfileRequest request) throws ServiceFaultException {
        log.info("Call from network -  getProfile for user with id : [" + request.getId() + "]");
        GetProfileResponse response = new GetProfileResponse();
        try {
            Optional<User> opt = profileManager.findById(request.getId(), UUID.fromString(request.getTokenUUID()));
            opt.ifPresent(response::setUser);
        } catch (Exception ex) {
            GenericExceptionHelper.serviceFaultExceptionHandler(ex);
        }
        return response;
    }

    /**
     * <p>This methods is exposed. It uses the RequestPayload to do a custom call to the Business layer.</p>
     *
     * <p>There is a verification on token UUID (the user must must possess a valid to perform this
     * method).</p>
     *
     * <p>Exceptions thrown by the Business layer (InvalidTokenException, OutdatedTokenException) are processed by the
     * serviceFaultExceptionHandler : depending the instance of the exception it builds a custom SOAP error.</p>
     *
     * @param request is an instance of ListAllProfilesRequest. It's mapped from the incoming SOAP message.
     * @return ListAllProfilesResponse the output message contains this response.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "listAllProfilesRequest")
    @ResponsePayload
    public ListAllProfilesResponse listAllProfiles(@RequestPayload ListAllProfilesRequest request) throws ServiceFaultException {
        log.info("Call from network - listAllProfiles to get a list of all user");
        ListAllProfilesResponse response = new ListAllProfilesResponse();
        List<User> users = response.getUsers();
        try {
            users.addAll(profileManager.findAll(UUID.fromString(request.getTokenUUID())));
        } catch (Exception ex) {
            GenericExceptionHelper.serviceFaultExceptionHandler(ex);
        }
        return response;
    }

    /**
     * <p>This methods is exposed. It uses the RequestPayload to do a custom call to the Business layer.</p>
     *
     * <p><b>Method's logic :</b> use when you need to know which are the users that detains late loan.</p>
     *
     * @param request is an instance of ListLateProfilesRequest. It's mapped from the incoming SOAP message.
     * @return ListLateProfilesResponse the output message contains this response.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "listLateProfilesRequest")
    @ResponsePayload
    public ListLateProfilesResponse ListLateProfiles(@RequestPayload ListLateProfilesRequest request) throws ServiceFaultException {
        log.info("Call from network - listLateProfiles to get a list of all late user");
        ListLateProfilesResponse response = new ListLateProfilesResponse();
        List<UserMin> users = response.getUsers();
        users.addAll(profileManager.findLatecomers());
        return response;
    }
}
