package com.stelmach.bartosz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@Getter @Setter
@ToString
public class Coordinates implements Serializable {
	private int rank;
	private int file;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Coordinates coordinates = (Coordinates) o;
		return rank == coordinates.rank && file == coordinates.file;
	}

	@Override
	public int hashCode() {
		return Objects.hash(rank, file);
	}
}
