/*
 * Copyright (c) 2012. The Genome Analysis Centre, Norwich, UK
 * MISO project contacts: Robert Davey @ TGAC
 * *********************************************************************
 *
 * This file is part of MISO.
 *
 * MISO is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MISO is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MISO. If not, see <http://www.gnu.org/licenses/>.
 *
 * *********************************************************************
 */

package uk.ac.bbsrc.tgac.miso.webapp.controller.rest;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.eaglegenomics.simlims.core.User;

import uk.ac.bbsrc.tgac.miso.core.data.InstrumentModel;
import uk.ac.bbsrc.tgac.miso.core.data.Library;
import uk.ac.bbsrc.tgac.miso.core.data.Pair;
import uk.ac.bbsrc.tgac.miso.core.data.Partition;
import uk.ac.bbsrc.tgac.miso.core.data.PartitionQCType;
import uk.ac.bbsrc.tgac.miso.core.data.Run;
import uk.ac.bbsrc.tgac.miso.core.data.RunPartition;
import uk.ac.bbsrc.tgac.miso.core.data.RunPartitionAliquot;
import uk.ac.bbsrc.tgac.miso.core.data.SequencerPartitionContainer;
import uk.ac.bbsrc.tgac.miso.core.data.impl.LibraryAliquot;
import uk.ac.bbsrc.tgac.miso.core.data.impl.RunPosition;
import uk.ac.bbsrc.tgac.miso.core.data.impl.RunPurpose;
import uk.ac.bbsrc.tgac.miso.core.data.impl.view.PoolElement;
import uk.ac.bbsrc.tgac.miso.core.data.impl.view.PoolableElementView;
import uk.ac.bbsrc.tgac.miso.core.data.type.HealthType;
import uk.ac.bbsrc.tgac.miso.core.data.type.PlatformType;
import uk.ac.bbsrc.tgac.miso.core.security.AuthorizationManager;
import uk.ac.bbsrc.tgac.miso.core.service.ContainerService;
import uk.ac.bbsrc.tgac.miso.core.service.ExperimentService;
import uk.ac.bbsrc.tgac.miso.core.service.LibraryAliquotService;
import uk.ac.bbsrc.tgac.miso.core.service.LibraryService;
import uk.ac.bbsrc.tgac.miso.core.service.PartitionQcTypeService;
import uk.ac.bbsrc.tgac.miso.core.service.RunPartitionAliquotService;
import uk.ac.bbsrc.tgac.miso.core.service.RunPartitionService;
import uk.ac.bbsrc.tgac.miso.core.service.RunPurposeService;
import uk.ac.bbsrc.tgac.miso.core.service.RunService;
import uk.ac.bbsrc.tgac.miso.core.service.exception.ValidationException;
import uk.ac.bbsrc.tgac.miso.core.util.IndexChecker;
import uk.ac.bbsrc.tgac.miso.core.util.LimsUtils;
import uk.ac.bbsrc.tgac.miso.core.util.PaginatedDataSource;
import uk.ac.bbsrc.tgac.miso.core.util.PaginationFilter;
import uk.ac.bbsrc.tgac.miso.core.util.SampleSheet;
import uk.ac.bbsrc.tgac.miso.core.util.WhineyConsumer;
import uk.ac.bbsrc.tgac.miso.core.util.WhineyFunction;
import uk.ac.bbsrc.tgac.miso.dto.ContainerDto;
import uk.ac.bbsrc.tgac.miso.dto.DataTablesResponseDto;
import uk.ac.bbsrc.tgac.miso.dto.Dtos;
import uk.ac.bbsrc.tgac.miso.dto.ExperimentDto;
import uk.ac.bbsrc.tgac.miso.dto.ExperimentDto.RunPartitionDto;
import uk.ac.bbsrc.tgac.miso.dto.InstrumentModelDto;
import uk.ac.bbsrc.tgac.miso.dto.PartitionDto;
import uk.ac.bbsrc.tgac.miso.dto.RunPartitionAliquotDto;
import uk.ac.bbsrc.tgac.miso.dto.StudyDto;
import uk.ac.bbsrc.tgac.miso.dto.request.DetailedQcStatusUpdateDto;
import uk.ac.bbsrc.tgac.miso.dto.run.RunDto;
import uk.ac.bbsrc.tgac.miso.webapp.controller.component.AdvancedSearchParser;

/**
 * A controller to handle all REST requests for Runs
 * 
 * @author Rob Davey
 * @date 01-Sep-2011
 * @since 0.1.0
 */
@Controller
@RequestMapping("/rest/runs")
public class RunRestController extends RestController {
  public static class RunPartitionQCRequest {
    private List<Long> partitionIds;
    private Long qcTypeId;
    private String notes;

    public List<Long> getPartitionIds() {
      return partitionIds;
    }

    public void setPartitionIds(List<Long> partitionIds) {
      this.partitionIds = partitionIds;
    }

    public Long getQcTypeId() {
      return qcTypeId;
    }

    public void setQcTypeId(Long qcTypeId) {
      this.qcTypeId = qcTypeId;
    }

    public String getNotes() {
      return notes;
    }

    public void setNotes(String notes) {
      this.notes = notes;
    }
  }

  public static class RunPartitionPurposeRequest {
    private List<Long> partitionIds;
    private Long runPurposeId;

    public List<Long> getPartitionIds() {
      return partitionIds;
    }

    public void setPartitionIds(List<Long> partitionIds) {
      this.partitionIds = partitionIds;
    }

    public Long getRunPurposeId() {
      return runPurposeId;
    }

    public void setRunPurposeId(Long runPurposeId) {
      this.runPurposeId = runPurposeId;
    }
  }

  public static class RunLibaryQcStatusUpdateRequest {

    private Boolean qcPassed;
    private String note;

    public Boolean getQcPassed() {
      return qcPassed;
    }

    public void setQcPassed(Boolean qcPassed) {
      this.qcPassed = qcPassed;
    }

    public String getNote() {
      return note;
    }

    public void setNote(String note) {
      this.note = note;
    }

  }

  @Autowired
  private AuthorizationManager authorizationManager;
  @Autowired
  private RunService runService;
  @Autowired
  private ContainerService containerService;
  @Autowired
  private RunPartitionService runPartitionService;
  @Autowired
  private PartitionQcTypeService partitionQcTypeService;
  @Autowired
  private RunPartitionAliquotService runPartitionAliquotService;
  @Autowired
  private LibraryService libraryService;
  @Autowired
  private LibraryAliquotService libraryAliquotService;
  @Autowired
  private ExperimentService experimentService;
  @Autowired
  private RunPurposeService runPurposeService;
  @Autowired
  private IndexChecker indexChecker;
  @Autowired
  private AdvancedSearchParser advancedSearchParser;

  private final JQueryDataTableBackend<Run, RunDto> jQueryBackend = new JQueryDataTableBackend<Run, RunDto>() {

    @Override
    protected RunDto asDto(Run model) {
      return Dtos.asDto(model);
    }

    @Override
    protected PaginatedDataSource<Run> getSource() throws IOException {
      return runService;
    }
  };

  @GetMapping(value = "/{runId}", produces = "application/json")
  public @ResponseBody RunDto getRunById(@PathVariable long runId) throws IOException {
    return RestUtils.getObject("Run", runId, runService, Dtos::asDto);
  }

  @GetMapping(value = "/{runId}/full", produces = "application/json")
  public @ResponseBody RunDto getRunByIdFull(@PathVariable Long runId) throws IOException {
    return RestUtils.getObject("Run", runId, runService, run -> Dtos.asDto(run, true, true, true));
  }

  @GetMapping(value = "/{runId}/containers", produces = "application/json")
  public @ResponseBody List<ContainerDto> getContainersByRunId(@PathVariable Long runId) throws IOException {
    Collection<SequencerPartitionContainer> cc = containerService.listByRunId(runId);
    return Dtos.asContainerDtos(cc, true, true);
  }

  @GetMapping(value = "/alias/{runAlias}", produces = "application/json")
  public @ResponseBody RunDto getRunByAlias(@PathVariable String runAlias) throws IOException {
    Run r = runService.getRunByAlias(runAlias);
    if (r == null) {
      throw new RestException("No run found with alias: " + runAlias, Status.NOT_FOUND);
    }
    return Dtos.asDto(r);
  }

  @GetMapping(value = "/{runId}/samplesheet/{sheet}")
  public HttpEntity<String> getSampleSheetForRun(@PathVariable(name = "runId") Long runId, @PathVariable(name = "sheet") String sheet,
      HttpServletResponse response) throws IOException {
    Run run = runService.get(runId);
    return getSampleSheetForRun(run, SampleSheet.valueOf(sheet), response);
  }

  @GetMapping(value = "/alias/{runAlias}/samplesheet/{sheet}")
  public HttpEntity<String> getSampleSheetForRunByAlias(@PathVariable(name = "runAlias") String runAlias,
      @PathVariable(name = "sheet") String sheet, HttpServletResponse response) throws IOException {
    Run run = runService.getRunByAlias(runAlias);
    return getSampleSheetForRun(run, SampleSheet.valueOf(sheet), response);
  }

  private HttpEntity<String> getSampleSheetForRun(Run run, SampleSheet casavaVersion, HttpServletResponse response) throws IOException {
    if (run == null) {
      throw new RestException("Run does not exist.", Status.NOT_FOUND);
    }
    User user = authorizationManager.getCurrentUser();
    if (run.getSequencerPartitionContainers().size() != 1) {
      throw new RestException(
          "Expected 1 sequencing container for run " + run.getAlias() + ", but found " + run.getSequencerPartitionContainers().size());
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("text", "csv"));
    response.setHeader("Content-Disposition",
        "attachment; filename=" + String.format("RUN%d-%s-SampleSheet.csv", run.getId(), casavaVersion.name()));

    return new HttpEntity<>(casavaVersion.createSampleSheet(run, user), headers);
  }

  @GetMapping(produces = "application/json")
  public @ResponseBody List<RunDto> listAllRuns() throws IOException {
    Collection<Run> lr = runService.list();
    return Dtos.asRunDtos(lr);
  }

  @GetMapping(value = "/dt", produces = "application/json")
  @ResponseBody
  public DataTablesResponseDto<RunDto> dataTable(HttpServletRequest request) throws IOException {
    return jQueryBackend.get(request, advancedSearchParser);
  }

  @GetMapping(value = "/dt/project/{id}", produces = "application/json")
  @ResponseBody
  public DataTablesResponseDto<RunDto> dataTableByProject(@PathVariable("id") Long id, HttpServletRequest request) throws IOException {
    return jQueryBackend.get(request, advancedSearchParser, PaginationFilter.project(id));
  }

  @GetMapping(value = "/dt/platform/{platform}", produces = "application/json")
  @ResponseBody
  public DataTablesResponseDto<RunDto> dataTableByPlatform(@PathVariable("platform") String platform, HttpServletRequest request)
      throws IOException {
    PlatformType platformType = PlatformType.valueOf(platform);
    if (platformType == null) {
      throw new RestException("Invalid platform type.", Status.BAD_REQUEST);
    }
    return jQueryBackend.get(request, advancedSearchParser, PaginationFilter.platformType(platformType));
  }

  @GetMapping(value = "/dt/sequencer/{id}", produces = "application/json")
  @ResponseBody
  public DataTablesResponseDto<RunDto> dataTableBySequencer(@PathVariable("id") Long id, HttpServletRequest request)
      throws IOException {
    return jQueryBackend.get(request, advancedSearchParser, PaginationFilter.sequencer(id));
  }

  @PostMapping(value = "{runId}/add", produces = "application/json")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void addContainerByBarcode(@PathVariable Long runId, @RequestParam("position") String position,
      @RequestParam("barcode") String barcode) throws IOException {
    Run run = runService.get(runId);
    Collection<SequencerPartitionContainer> containers = containerService
        .listByBarcode(barcode);
    if (containers.isEmpty()) {
      throw new RestException("No containers with this barcode.", Status.BAD_REQUEST);
    }
    if (containers.size() > 1) {
      throw new RestException("Multiple containers with this barcode.", Status.BAD_REQUEST);
    }
    SequencerPartitionContainer container = containers.iterator().next();
    InstrumentModel runPlatform = run.getSequencer().getInstrumentModel();
    if (container.getModel().getInstrumentModels().stream()
        .noneMatch(platform -> platform.getId() == runPlatform.getId())) {
      throw new RestException(String.format("Container model '%s' (%s) is not compatible with %s (%s) run",
          container.getModel().getAlias(), container.getModel().getPlatformType(), run.getSequencer().getInstrumentModel().getAlias(),
          run.getSequencer().getInstrumentModel().getPlatformType()), Status.BAD_REQUEST);
    }
    RunPosition runPos = new RunPosition();
    runPos.setRun(run);
    runPos.setContainer(container);
    if (!LimsUtils.isStringEmptyOrNull(position)) {
      runPos.setPosition(runPlatform.getPositions().stream()
          .filter(pos -> pos.getAlias().equals(position))
          .findFirst().orElseThrow(() -> new ValidationException(
              String.format("Platform %s does not have a position %s", runPlatform.getAlias(), position))));
    }
    run.getRunPositions().add(runPos);
    runService.update(run);
  }

  @PostMapping(value = "/{runId}/remove", produces = "application/json")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void removeContainer(@PathVariable Long runId, @RequestBody List<Long> containerIds) throws IOException {
    Run run = runService.get(runId);
    run.getRunPositions().removeIf(rp -> containerIds.contains(rp.getContainer().getId()));
    runService.update(run);
  }

  @PostMapping(value = "/{runId}/qc", produces = "application/json")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void setQc(@PathVariable Long runId, @RequestBody RunPartitionQCRequest request) throws IOException {
    Run run = RestUtils.retrieve("Run", runId, runService);
    PartitionQCType qcType = partitionQcTypeService.list().stream().filter(qt -> qt.getId() == request.getQcTypeId()).findAny()
        .orElseThrow(() -> new RestException(Status.BAD_REQUEST));
    run.getSequencerPartitionContainers().stream()//
        .flatMap(container -> container.getPartitions().stream())//
        .filter(partition -> request.partitionIds.contains(partition.getId()))//
        .map(WhineyFunction.rethrow(partition -> {
          RunPartition runPartition = runPartitionService.get(run, partition);
          if (runPartition == null) {
            runPartition = new RunPartition();
            runPartition.setRun(run);
            runPartition.setPartition(partition);
          }
          runPartition.setQcType(qcType);
          runPartition.setNotes(request.notes);
          return runPartition;
        })).forEach(WhineyConsumer.rethrow(runPartitionService::save));
  }

  @PutMapping("/{runId}/partition-purposes")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setPartitionPurposes(@PathVariable long runId, @RequestBody RunPartitionPurposeRequest request) throws IOException {
    Run run = RestUtils.retrieve("Run", runId, runService);
    RunPurpose purpose = RestUtils.retrieve("Run purpose", request.getRunPurposeId(), runPurposeService);
    List<Partition> partitions = run.getSequencerPartitionContainers().stream()
        .flatMap(container -> container.getPartitions().stream())
        .filter(partition -> request.getPartitionIds().contains(partition.getId()))
        .collect(Collectors.toList());
    for (Partition partition : partitions) {
      RunPartition runPartition = runPartitionService.get(run, partition);
      if (runPartition == null) {
        runPartition = new RunPartition();
        runPartition.setRun(run);
        runPartition.setPartition(partition);
      }
      runPartition.setPurpose(purpose);
      runPartitionService.save(runPartition);
    }
  }

  @PutMapping("/{runId}/aliquots")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void saveAliquots(@PathVariable long runId, @RequestBody List<RunPartitionAliquotDto> dtos) throws IOException {
    RestUtils.retrieve("Run", runId, runService);
    List<RunPartitionAliquot> runPartitionAliquots = dtos.stream().map(Dtos::to).collect(Collectors.toList());
    runPartitionAliquotService.save(runPartitionAliquots);
  }

  public static class StudiesForExperiment {
    private ExperimentDto experiment;
    private List<StudyDto> studies;

    public ExperimentDto getExperiment() {
      return experiment;
    }

    public List<StudyDto> getStudies() {
      return studies;
    }

    public void setExperiment(ExperimentDto experiment) {
      this.experiment = experiment;
    }

    public void setStudies(List<StudyDto> studies) {
      this.studies = studies;
    }
  }

  @GetMapping("/{runId}/potentialExperiments")
  public @ResponseBody List<StudiesForExperiment> getPotentialExperiments(@PathVariable long runId) throws IOException {
    Run run = getRun(runId);

    RunDto runDto = Dtos.asDto(run);
    InstrumentModelDto instrumentModelDto = Dtos.asDto(run.getSequencer().getInstrumentModel());
    Map<Library, List<Partition>> libraryGroups = getLibraryGroups(run);

    return libraryGroups.entrySet().stream().map(group -> new Pair<>(group.getKey(),
        group.getValue().stream().map(partition -> Dtos.asDto(partition, indexChecker))
            .map(partitionDto -> new RunPartitionDto(runDto, partitionDto))
            .collect(Collectors.toList())))
        .map(group -> {
          StudiesForExperiment result = new StudiesForExperiment();
          result.experiment = new ExperimentDto();
          result.experiment.setLibrary(Dtos.asDto(group.getKey(), false));
          result.experiment.setInstrumentModel(instrumentModelDto);
          result.experiment.setPartitions(group.getValue());

          result.studies = group.getKey().getSample().getProject().getStudies().stream().map(Dtos::asDto).collect(Collectors.toList());
          return result;
        }).collect(Collectors.toList());
  }

  public static class AddRequest {
    private ExperimentDto experiment;
    private PartitionDto partition;

    public ExperimentDto getExperiment() {
      return experiment;
    }

    public PartitionDto getPartition() {
      return partition;
    }

    public void setExperiment(ExperimentDto experiment) {
      this.experiment = experiment;
    }

    public void setPartition(PartitionDto partition) {
      this.partition = partition;
    }
  }

  @GetMapping("/{runId}/potentialExperimentExpansions")
  public @ResponseBody List<AddRequest> getPotentialExperimentExpansions(@PathVariable long runId) throws IOException {
    Run run = getRun(runId);

    Map<Library, List<Partition>> libraryGroups = getLibraryGroups(run);

    return libraryGroups.entrySet().stream()
        .<AddRequest> flatMap(WhineyFunction.rethrow(group -> //
        experimentService.listAllByLibraryId(group.getKey().getId()).stream()//
            .filter(experiment -> experiment.getInstrumentModel().getId() == run.getSequencer().getInstrumentModel().getId())
            .flatMap(experiment -> group.getValue().stream()//
                .filter(partition -> experiment.getRunPartitions().stream().noneMatch(rp -> rp.getPartition().equals(partition)))
                .map(partition -> {
                  AddRequest request = new AddRequest();
                  request.experiment = Dtos.asDto(experiment);
                  request.partition = Dtos.asDto(partition, indexChecker);
                  return request;
                }))))
        .collect(Collectors.toList());
  }

  private Map<Library, List<Partition>> getLibraryGroups(Run run) {
    return run.getSequencerPartitionContainers().stream()//
        .flatMap(container -> container.getPartitions().stream())//
        .filter(partition -> partition.getPool() != null)//
        .flatMap(partition -> partition.getPool().getPoolContents().stream()//
            .map(PoolElement::getPoolableElementView)//
            .map(PoolableElementView::getLibraryId)//
            .distinct()
            .map(libraryId -> new Pair<>(libraryId, partition)))//
        .collect(Collectors.groupingBy(Pair::getKey))//
        .entrySet().stream()//
        .collect(Collectors.toMap(//
            WhineyFunction
                .rethrow(entry -> libraryService.get(entry.getKey())), //
            entry -> entry.getValue().stream()//
                .map(Pair::getValue)//
                .collect(Collectors.toList())));
  }

  @GetMapping("/search")
  public @ResponseBody List<RunDto> searchRuns(@RequestParam("q") String query) throws IOException {
    return runService.listBySearch(query).stream().map(Dtos::asDto).collect(Collectors.toList());
  }

  @GetMapping("/recent")
  public @ResponseBody List<RunDto> listRecentRuns() throws IOException {
    return runService.list(0, 50, false, "startDate").stream().map(Dtos::asDto).collect(Collectors.toList());
  }

  private Run getRun(long runId) throws IOException {
    Run run = runService.get(runId);
    if (run == null) {
      throw new RestException("Run not found", Status.NOT_FOUND);
    }
    return run;
  }

  @PostMapping
  public @ResponseBody RunDto createRun(@RequestBody RunDto dto) throws IOException {
    return RestUtils.createObject("Run", dto, Dtos::to, runService, Dtos::asDto);
  }

  @PutMapping("/{runId}")
  public @ResponseBody RunDto updateRun(@PathVariable long runId, @RequestBody RunDto dto) throws IOException {
    return RestUtils.updateObject("Run", runId, dto, WhineyFunction.rethrow(d -> {
      Run run = Dtos.to(d);
      // Containers cannot be updated in this way
      Run existing = runService.get(runId);
      run.getRunPositions().clear();
      for (RunPosition pos : existing.getRunPositions()) {
        run.addSequencerPartitionContainer(pos.getContainer(), pos.getPosition());
      }
      return run;
    }), runService, Dtos::asDto);
  }

  @PostMapping(value = "/bulk-delete")
  @ResponseBody
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void bulkDelete(@RequestBody(required = true) List<Long> ids) throws IOException {
    RestUtils.bulkDelete("Run", ids, runService);
  }

  @PutMapping("/{runId}/status")
  public @ResponseBody RunDto updateStatus(@PathVariable long runId, @RequestParam String status) throws IOException {
    Run run = RestUtils.retrieve("Run", runId, runService);
    HealthType health = HealthType.get(status);
    if (health == null) {
      throw new RestException("Invalid run status: " + status, Status.BAD_REQUEST);
    }
    run.setHealth(health);
    long savedId = runService.update(run);
    Run saved = runService.get(savedId);
    return Dtos.asDto(saved);
  }

  @PutMapping("/{runIdId}/partitions/{partitionId}/qc-status")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public @ResponseBody void updateRunPartitionQcStatus(@PathVariable long runId, @PathVariable long partitionId,
      @RequestBody DetailedQcStatusUpdateDto dto) throws IOException {
    Run run = RestUtils.retrieve("Run", runId, runService);
    Partition partition = findPartitionInRun(run, partitionId);
    PartitionQCType status = RestUtils.retrieve("Partition QC status", dto.getQcStatusId(), partitionQcTypeService, Status.BAD_REQUEST);

    RunPartition runPartition = runPartitionService.get(run, partition);
    if (runPartition == null) {
      runPartition = new RunPartition();
      runPartition.setRun(run);
      runPartition.setPartition(partition);
    }
    runPartition.setQcType(status);
    runPartition.setNotes(dto.getNote());
    runPartitionService.save(runPartition);
  }

  private Partition findPartitionInRun(Run run, long partitionId) {
    Partition partition = run.getRunPositions().stream()
        .map(RunPosition::getContainer)
        .flatMap(x -> x.getPartitions().stream())
        .filter(x -> x.getId() == partitionId)
        .findFirst().orElse(null);
    if (partition == null) {
      throw new RestException(String.format("Partition %d not found in run", partitionId), Status.NOT_FOUND);
    }
    return partition;
  }

  @PutMapping("/{runIdId}/partitions/{partitionId}/libraryaliquots/{aliquotId}/qc-status")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public @ResponseBody void updateRunLibraryQcStatus(@PathVariable long runId, @PathVariable long partitionId,
      @PathVariable long aliquotId, @RequestBody RunLibaryQcStatusUpdateRequest dto) throws IOException {
    Run run = RestUtils.retrieve("Run", runId, runService);
    Partition partition = findPartitionInRun(run, partitionId);
    PoolableElementView poolableElementView = partition.getPool().getPoolContents().stream()
        .map(PoolElement::getPoolableElementView)
        .filter(x -> x.getId() == aliquotId)
        .findFirst().orElse(null);
    if (poolableElementView == null) {
      throw new RestException(String.format("Library aliquot %d not found in partition", aliquotId), Status.NOT_FOUND);
    }
    LibraryAliquot aliquot = RestUtils.retrieve("Library aliquot", aliquotId, libraryAliquotService);

    RunPartitionAliquot runLib = runPartitionAliquotService.get(run, partition, aliquot);
    if (runLib == null) {
      runLib = new RunPartitionAliquot();
      runLib.setRun(run);
      runLib.setPartition(partition);
      runLib.setAliquot(aliquot);
    }
    runLib.setQcPassed(dto.getQcPassed());
    runLib.setQcNote(dto.getNote());
    runPartitionAliquotService.save(runLib);
  }

}