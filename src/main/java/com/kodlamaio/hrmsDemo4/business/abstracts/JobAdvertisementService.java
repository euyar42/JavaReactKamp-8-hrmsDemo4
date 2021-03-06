package com.kodlamaio.hrmsDemo4.business.abstracts;

import java.util.List;

import com.kodlamaio.hrmsDemo4.core.utilities.result.concretes.DataResult;
import com.kodlamaio.hrmsDemo4.core.utilities.result.concretes.Result;
import com.kodlamaio.hrmsDemo4.entities.concretes.JobAdvertisement;

public interface JobAdvertisementService {
	Result add(JobAdvertisement jobAdvertisement);
	DataResult<List<JobAdvertisement>> getByActivated();
	DataResult<List<JobAdvertisement>> getByActivatedOrderByReleaseDateAsc();
	DataResult<List<JobAdvertisement>> getByActivatedOrderByApplicationDeadlineAsc();
	DataResult<List<JobAdvertisement>> getByActivatedAndEmployerId(Integer id);
	
	Result activateJobAdvertisement(Integer id);
	Result deactivateJobAdvertisement(Integer id);
}
