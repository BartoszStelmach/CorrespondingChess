package com.stelmach.bartosz.game.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	private String whiteName;
	private String blackName;

	@Transient
	@JsonInclude
	private List<String> moves = new ArrayList<>();

	public Game(String whiteName, String blackName) {
		this.whiteName = whiteName;
		this.blackName = blackName;
	}
}
