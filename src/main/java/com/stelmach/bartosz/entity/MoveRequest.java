package com.stelmach.bartosz.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static com.stelmach.bartosz.entity.Board.PieceColour;

@NoArgsConstructor @Getter
public class MoveRequest implements Serializable {
	private int gameId;
	@NotBlank @Size(min = 2, max = 7) @Pattern(regexp = "[KQBNRP1-8a-hx+#O=-]+")
	private String notation;
	private PieceColour colour;
}
