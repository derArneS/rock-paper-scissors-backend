package de.arnes.rockpaperscissorsbackend.model.users.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 
 * @author Arne S.
 *
 */
@Data
@Table
@Entity
public class UserStatistics {
	
	@Id
	@Column
	private String id;
	
	@Column
	private int rock;
	
	@Column
	private int paper;
	
	@Column
	private int scissors;
	
	@Column
	private int computerRock;
	
	@Column
	private int computerPaper;
	
	@Column
	private int computerScissors;
	
	public void addRock() {
		this.rock = rock + 1;
	}
	
	public void addPaper() {
		this.paper = paper + 1;
	}
	
	public void addScissors() {
		this.scissors = scissors + 1;
	}
	
	public void addComputerRock() {
		this.computerRock = computerRock + 1;
	}
	
	public void addComputerPaper() {
		this.computerPaper = computerPaper + 1;
	}
	
	public void addComputerScissors() {
		this.computerScissors = computerScissors + 1;
	}
	

}
