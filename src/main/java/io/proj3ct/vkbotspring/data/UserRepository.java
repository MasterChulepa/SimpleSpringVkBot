package io.proj3ct.vkbotspring.data;

import io.proj3ct.vkbotspring.module.UserModel;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository <UserModel, Long>{
}
