package it.unibz.testhunter.plugin.hudson.siaps;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import it.unibz.testhunter.plugin.hudson.IUrlResolver;
import it.unibz.testhunter.shared.IBuild;
import it.unibz.testhunter.shared.IJob;
import it.unibz.testhunter.shared.TException;

public class BuildImpl implements IBuild {

	private final Long number;
	private final String result;
	private final Timestamp start;
	private final List<IJob> jobs;

	@Inject
	public BuildImpl(IJobFactory jobFactory,
			@Named("build") IUrlResolver urlResolver, @Assisted URL url,
			@Assisted Long number, @Assisted Timestamp start,
			@Assisted String result) throws TException {
		this.number = number;
		this.start = start;
		this.result = result;
		this.jobs = new ArrayList<IJob>();
		this.jobs.add(jobFactory.create(urlResolver.resolve(url)));
	}

	@Override
	public Collection<IJob> getAllJobs() throws TException {
		return jobs;
	}

	@Override
	public IJob getJob(int index) throws TException {
		try {
			return jobs.get(index);
		} catch (IndexOutOfBoundsException e) {
			throw new TException(e.getMessage()).setTerminateApp().setUserMsg(
					"job list index is out of bounds");
		}

	}

	@Override
	public int getJobCount() throws TException {
		return jobs.size();
	}

	@Override
	public Long getNumber() {
		return number;
	}

	@Override
	public Timestamp getStart() {
		return start;
	}

	@Override
	public String getResult() {
		return result;
	}

	@Override
	public int compareTo(IBuild o) {
		try {
			return this.getNumber().compareTo(o.getNumber());
		} catch (TException e) {
			return 0;
		} 
	}

}
