package io.pivotal.pal.tracker;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
public class TimeEntryController {

	private TimeEntryRepository timeEntryRepository;
	private final DistributionSummary timeEntrySummary;
	private final Counter actionCounter;

	public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
		
		this.timeEntryRepository=timeEntryRepository;
		timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
	}

	@PostMapping("/time-entries")
	public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
		TimeEntry createResponse = timeEntryRepository.create(timeEntryToCreate);
		actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
		return new ResponseEntity<TimeEntry>(createResponse,HttpStatus.CREATED);
	}
	
	@GetMapping("/time-entries/{id}")
	public ResponseEntity<TimeEntry> read(@PathVariable("id") long timeEntryId) {
		TimeEntry readResponse = timeEntryRepository.find(timeEntryId);
		if(readResponse!=null) {
			 actionCounter.increment();
			return new ResponseEntity<TimeEntry>(readResponse,HttpStatus.OK);
		}else {
			return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
		}
		
		
	}
	
	@GetMapping("/time-entries")
	public ResponseEntity<List<TimeEntry>> list() {
		List<TimeEntry> listcreateResponse = timeEntryRepository.list();
		actionCounter.increment();
		return new ResponseEntity<List<TimeEntry>>(listcreateResponse,HttpStatus.OK);
	}

	@PutMapping("/time-entries/{id}")
	public ResponseEntity<TimeEntry> update(@PathVariable("id") long timeEntryId, @RequestBody TimeEntry expected) {
		TimeEntry updateResponse = timeEntryRepository.update(timeEntryId, expected);
		if(updateResponse!=null) {
			actionCounter.increment();
			return new ResponseEntity<TimeEntry>(updateResponse,HttpStatus.OK);
		}else {
			return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/time-entries/{id}")
	public ResponseEntity<TimeEntry> delete(@PathVariable("id") long timeEntryId) {
		timeEntryRepository.delete(timeEntryId);
		actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
		return new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
	}

}
