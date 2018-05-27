package com.example.producer;

public interface UserRepository {

	void save(User user);

	User load();
}
