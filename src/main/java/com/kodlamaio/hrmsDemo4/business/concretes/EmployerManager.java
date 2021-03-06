package com.kodlamaio.hrmsDemo4.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.hrmsDemo4.business.abstracts.EmployerService;
import com.kodlamaio.hrmsDemo4.core.utilities.result.concretes.DataResult;
import com.kodlamaio.hrmsDemo4.core.utilities.result.concretes.ErrorDataResult;
import com.kodlamaio.hrmsDemo4.core.utilities.result.concretes.ErrorResult;
import com.kodlamaio.hrmsDemo4.core.utilities.result.concretes.Result;
import com.kodlamaio.hrmsDemo4.core.utilities.result.concretes.SuccessDataResult;
import com.kodlamaio.hrmsDemo4.core.utilities.result.concretes.SuccessResult;
import com.kodlamaio.hrmsDemo4.core.validators.emailRegex.abstracts.EmployerEmailRegexValidatorService;
import com.kodlamaio.hrmsDemo4.core.validators.emailVerify.abstracts.EmployerEmailVerifyService;
import com.kodlamaio.hrmsDemo4.core.validators.systemEmployeeVerify.abstracts.EmployerSystemEmployeeVerifyService;
import com.kodlamaio.hrmsDemo4.dataAccess.abstracts.EmployerDao;
import com.kodlamaio.hrmsDemo4.entities.concretes.Employer;

@Service
public class EmployerManager implements EmployerService {

	private EmployerDao employerDao;
	private EmployerEmailRegexValidatorService employerEmailRegexValidatorService;
	private EmployerEmailVerifyService employerEmailVerifyService;
	private EmployerSystemEmployeeVerifyService employerSystemEmployeeVerifyService;

	@Autowired
	public EmployerManager(EmployerDao employerDao,
			EmployerEmailRegexValidatorService employerEmailRegexValidatorService, 
			EmployerEmailVerifyService employerEmailVerifyService, 
			EmployerSystemEmployeeVerifyService employerSystemEmployeeVerifyService) {
		this.employerDao = employerDao;
		this.employerEmailRegexValidatorService = employerEmailRegexValidatorService;
		this.employerEmailVerifyService = employerEmailVerifyService;
		this.employerSystemEmployeeVerifyService = employerSystemEmployeeVerifyService;
	}

	@Override
	public DataResult<List<Employer>> getAll() {
		return new SuccessDataResult<List<Employer>>("????verenler ba??ar??yla listelendi.", this.employerDao.findAll());
	}

	@Override
	public DataResult<Employer> get(int id) {
		if (this.employerDao.findById(id).orElse(null) != null) {
			return new SuccessDataResult<Employer>("Belirtilen i?? pozisyonu ba??ar??yla bulundu.",
					this.employerDao.findById(id).get());
		} else {
			return new ErrorDataResult<Employer>("Belirtilen i?? pozisyonu mevcut de??ildir.");
		}
	}

	@Override
	public Result add(Employer employer) {
		if (this.hasEmptyField(employer)) {
			return new ErrorResult("T??m alanlar zorunludur.");
		} else if (!this.employerEmailRegexValidatorService.isValidEmail(employer.getEmail(),
				employer.getWebSite())) {
			return new ErrorResult("Email, web site ile ayn?? domain'e sahip olmal??d??r.");
		} else if (this.existsEmployerByEmail(employer.getEmail())) {
			return new ErrorResult("Bu email'e sahip bir i??veren kayd?? mevcuttur.");
		} else if (!this.employerEmailVerifyService.hasVerifyEmail(employer.getEmail())) {
			return new ErrorResult("Email do??rulanmad??!");
		} else if (!this.employerSystemEmployeeVerifyService.hasVerifyBySystemEmployee(employer)) {
			return new ErrorResult("Sistem taraf??ndan do??rulanmad??!");
		} else {
			this.employerDao.save(employer);
			return new SuccessResult("????veren ba??ar??yla kaydedildi.");
		}
	}

	@Override
	public Result delete(int id) {
		this.employerDao.deleteById(id);
		return new SuccessResult("????veren ba??ar??yla silindi.");
	}

	@Override
	public Result update(Employer employer) {
		this.employerDao.save(employer);
		return new SuccessResult("????veren ba??ar??yla g??ncellendi.");
	}

	@Override
	public boolean existsEmployerByEmail(String email) {
		return this.employerDao.existsEmployerByEmail(email);
	}

	@Override
	public boolean hasEmptyField(Employer employer) {
		if (employer.getCompanyName().isEmpty() || employer.getWebSite().isEmpty()
				|| employer.getEmail().isEmpty() || employer.getPassword().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

}
