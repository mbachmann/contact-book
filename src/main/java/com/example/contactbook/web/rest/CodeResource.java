package com.example.contactbook.web.rest;

import com.example.contactbook.model.codes.Code;
import com.example.contactbook.model.enums.CodeType;
import com.example.contactbook.service.CodeService;
import com.example.contactbook.utils.HasLogger;
import com.example.contactbook.web.rest.exception.BadRequestAlertException;
import com.example.contactbook.web.rest.utils.HeaderUtil;
import com.example.contactbook.web.rest.utils.PaginationUtil;
import com.example.contactbook.web.rest.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "Configurations")
public class CodeResource implements HasLogger {

    @Autowired
    CodeService codeService;

    private static final String ENTITY_NAME = "code";

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * {@code GET  /codes} : get all the codes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of codes in body.
     */
    @PageableAsQueryParam
    @GetMapping("/codes")
    public ResponseEntity<List<Code>> getAllCodeTypes(
            @Parameter(description = "The CodeType like AllCodes, EmailType, PhoneType or AddressType") @RequestParam(required = true, defaultValue = "AllCodes") CodeType codeType,
            @PageableDefault(size = 20, sort = {"shortCut", "title"}, direction = Sort.Direction.ASC) @Parameter(hidden = true) Pageable pageable) {

        getLogger().debug("REST request to get a page of Codes");

        Page<Code> page = codeService.findAllCodesByType(codeType, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /codes/:id} : get the "id" code.
     *
     * @param id the id of the code to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the code, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/codes/{id}")
    public ResponseEntity<Code> getCode(@PathVariable Long id) {
       getLogger().debug("REST request to get Code : {}", id);
       Optional<Code> codeOptional = codeService.findByIdWithUsage(id);
       return ResponseUtil.wrapOrNotFound(codeOptional);
    }

    /**
     * {@code POST  /codes} : Create a new code.
     *
     * @param code the code to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new code, or with status {@code 400 (Bad Request)} if the code has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/codes")
    public ResponseEntity<Code> createCode(
            @Parameter(description = "The CodeType like EmailType, PhoneType or AddressType") @RequestParam(required = true, defaultValue = "AddressType") CodeType codeType,
            @Valid @RequestBody Code code
    )
            throws URISyntaxException {
        getLogger().debug("REST request to save Code : {}", code);
        if (code.getId() != null) {
            throw new BadRequestAlertException("A new code cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Code result = save(codeType, code);

        return ResponseEntity
                .created(new URI("/api/codes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /codes/:id} : Updates an existing code.
     *
     * @param id the id of the code to save.
     * @param code the code to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated code,
     * or with status {@code 400 (Bad Request)} if the code is not valid,
     * or with status {@code 500 (Internal Server Error)} if the code couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/codes/{id}")
    public ResponseEntity<Code> updateCode(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody Code code
    ) throws URISyntaxException {
        getLogger().debug("REST request to update Code : {}, {}", id, code);
        if (code.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, code.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codeService.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<Code> codeOptional = codeService.findById(id);
        Code result = null;
        if (codeOptional.isPresent()) {
            CodeType codeType = CodeType.valueOf(codeOptional.get().getType());
            result = update(codeType, code);
        }

        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, code.getId().toString()))
                .body(result);
    }


    /**
     * {@code DELETE  /contact-codes/:id} : delete the "id" code.
     *
     * @param id the id of the code to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/codes/{id}")
    public ResponseEntity<Boolean> deleteCode(@PathVariable Long id) {
        getLogger().debug("REST request to delete Code : {}", id);
        Boolean result = codeService.deleteById(id);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .body(result);
    }

    private Code update (CodeType codeType, Code code) {
        return codeService.update(codeType, code);

    }

    private Code save (CodeType codeType, Code code) {
        return codeService.save(codeType, code);

    }

}
