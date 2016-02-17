/**
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.hawkbit.rest.resource.api;

import java.util.List;

import org.eclipse.hawkbit.rest.resource.RestConstants;
import org.eclipse.hawkbit.rest.resource.model.softwaremoduletype.SoftwareModuleTypePagedList;
import org.eclipse.hawkbit.rest.resource.model.softwaremoduletype.SoftwareModuleTypeRequestBodyPost;
import org.eclipse.hawkbit.rest.resource.model.softwaremoduletype.SoftwareModuleTypeRequestBodyPut;
import org.eclipse.hawkbit.rest.resource.model.softwaremoduletype.SoftwareModuleTypeRest;
import org.eclipse.hawkbit.rest.resource.model.softwaremoduletype.SoftwareModuleTypesRest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * REST Resource handling for SoftwareModule and related Artifact CRUD
 * operations.
 *
 */
@RequestMapping(RestConstants.SOFTWAREMODULETYPE_V1_REQUEST_MAPPING)
public interface SoftwareModuleTypeRestApi {
    /**
     * Handles the GET request of retrieving all SoftwareModuleTypes .
     *
     * @param pagingOffsetParam
     *            the offset of list of modules for pagination, might not be
     *            present in the rest request then default value will be applied
     * @param pagingLimitParam
     *            the limit of the paged request, might not be present in the
     *            rest request then default value will be applied
     * @param sortParam
     *            the sorting parameter in the request URL, syntax
     *            {@code field:direction, field:direction}
     * @param rsqlParam
     *            the search parameter in the request URL, syntax
     *            {@code q=name==abc}
     *
     * @return a list of all module type for a defined or default page request
     *         with status OK. The response is always paged. In any failure the
     *         JsonResponseExceptionHandler is handling the response.
     */
    @RequestMapping(method = RequestMethod.GET, produces = { "application/hal+json", MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<SoftwareModuleTypePagedList> getTypes(
            @RequestParam(value = RestConstants.REQUEST_PARAMETER_PAGING_OFFSET, defaultValue = RestConstants.REQUEST_PARAMETER_PAGING_DEFAULT_OFFSET) final int pagingOffsetParam,
            @RequestParam(value = RestConstants.REQUEST_PARAMETER_PAGING_LIMIT, defaultValue = RestConstants.REQUEST_PARAMETER_PAGING_DEFAULT_LIMIT) final int pagingLimitParam,
            @RequestParam(value = RestConstants.REQUEST_PARAMETER_SORTING, required = false) final String sortParam,
            @RequestParam(value = RestConstants.REQUEST_PARAMETER_SEARCH, required = false) final String rsqlParam);

    /**
     * Handles the GET request of retrieving a single software module type .
     *
     * @param softwareModuleTypeId
     *            the ID of the module type to retrieve
     *
     * @return a single softwareModule with status OK.
     * @throws EntityNotFoundException
     *             in case no with the given {@code softwareModuleId} exists.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{softwareModuleTypeId}", produces = { "application/hal+json",
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<SoftwareModuleTypeRest> getSoftwareModuleType(
            @PathVariable("softwareModuleTypeId") final Long softwareModuleTypeId);

    /**
     * Handles the DELETE request for a single software module type .
     *
     * @param softwareModuleTypeId
     *            the ID of the module to retrieve
     * @return status OK if delete as successfully.
     *
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{softwareModuleTypeId}")
    public ResponseEntity<Void> deleteSoftwareModuleType(
            @PathVariable("softwareModuleTypeId") final Long softwareModuleTypeId);

    /**
     * Handles the PUT request of updating a software module type .
     *
     * @param softwareModuleTypeId
     *            the ID of the software module in the URL
     * @param restSoftwareModuleType
     *            the module type to be updated.
     * @return status OK if update is successful
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{softwareModuleTypeId}", consumes = { "application/hal+json",
            MediaType.APPLICATION_JSON_VALUE }, produces = { "application/hal+json", MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<SoftwareModuleTypeRest> updateSoftwareModuleType(
            @PathVariable("softwareModuleTypeId") final Long softwareModuleTypeId,
            @RequestBody final SoftwareModuleTypeRequestBodyPut restSoftwareModuleType);

    /**
     * Handles the POST request of creating new SoftwareModuleTypes. The request
     * body must always be a list of types.
     *
     * @param softwareModuleTypes
     *            the modules to be created.
     * @return In case all modules could successful created the ResponseEntity
     *         with status code 201 - Created but without ResponseBody. In any
     *         failure the JsonResponseExceptionHandler is handling the
     *         response.
     */
    @RequestMapping(method = RequestMethod.POST, consumes = { "application/hal+json",
            MediaType.APPLICATION_JSON_VALUE }, produces = { "application/hal+json", MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<SoftwareModuleTypesRest> createSoftwareModuleTypes(
            @RequestBody final List<SoftwareModuleTypeRequestBodyPost> softwareModuleTypes);

}