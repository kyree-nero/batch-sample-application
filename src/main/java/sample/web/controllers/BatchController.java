package sample.web.controllers;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/*
 * Spring batch admin is deprecated.  Instead here is a small harness start jobs.
 */
@RestController
public class BatchController {
	@Autowired @Qualifier("simpleBatchJob") Job simpleBatchJob;
	@Autowired  @Qualifier("simplePartitionBatchJob") Job simplePartitionBatchJob;
	@Autowired  @Qualifier("simpleRemotePartitionBatchJob") Job simpleRemotePartitionBatchJob;
	@Autowired JobLauncher jobLauncher;
	@Autowired JobExplorer jobExplorer;
//	@Autowired JobOperator jobOperator;
	
	
	@GetMapping(path = "batch/simplebatchjob/start", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public  Long startSimpleBatchJob() throws JobExecutionException {
		JobParameters jobParameters = new JobParametersBuilder().addLong("unq", System.nanoTime()).toJobParameters();
		System.out.println(jobParameters);
		JobExecution jobExecution = jobLauncher.run(simpleBatchJob, jobParameters);
		return jobExecution.getId();
//		return jobOperator.startNextInstance(simpleBatchJob.getName());
	}
	
	@GetMapping(path = "batch/simplepartitioningbatchjob/start", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public  Long startSimplePartitionBatchJob() throws JobExecutionException {
		JobParameters jobParameters = new JobParametersBuilder().addLong("unq", System.nanoTime()).toJobParameters();
		System.out.println(jobParameters);
		JobExecution jobExecution = jobLauncher.run(simplePartitionBatchJob, jobParameters);
		return jobExecution.getId();
//		return jobOperator.startNextInstance(simplePartitionBatchJob.getName());
	}
	
	@GetMapping(path = "batch/simpleremotepartitioningbatchjob/start", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public  Long startSimpleRemotePartitionBatchJob() throws JobExecutionException {
		JobParameters jobParameters = new JobParametersBuilder().addLong("unq", System.nanoTime()).toJobParameters();
		System.out.println(jobParameters);
		JobExecution jobExecution = jobLauncher.run(simpleRemotePartitionBatchJob, jobParameters);
		return jobExecution.getId();
//		return jobOperator.startNextInstance(simpleRemotePartitionBatchJob.getName());
	}
	
	@GetMapping(path = "batch/status/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public  String findJobExecution(@PathVariable Long id) throws JobExecutionException {
		JobExecution jobExecution = jobExplorer.getJobExecution(id);
		return "{\"status\":\""+jobExecution.getStatus().name()+"\"}";
	
	}
}
