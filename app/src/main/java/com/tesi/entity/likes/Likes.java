package com.tesi.entity.likes;

import java.io.Serializable;
import java.util.Objects;

public class Likes implements Serializable {

	private Id id;

	public Likes() {}

	public Likes(Id id) {
		this.id=id;
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Likes likes = (Likes) o;
		return Objects.equals(id, likes.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
