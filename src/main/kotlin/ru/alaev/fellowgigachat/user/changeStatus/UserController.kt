package ru.alaev.fellowgigachat.user.changeStatus

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.alaev.fellowgigachat.chat.persistence.user.UserStorage
import ru.alaev.fellowgigachat.chat.persistence.user.postgres.model.UserEntity
import ru.alaev.fellowgigachat.config.DomainException
import ru.alaev.fellowgigachat.config.ErrorType.NOT_FOUND
import ru.alaev.fellowgigachat.domain.Username

@RestController
class UserController(
    private val userStorage: UserStorage
) {

    @GetMapping("/users/{username}")
    fun getUser(@PathVariable username: String): UserResponse {
        log.info("Requesting user info for :: $username")
        return UserResponse.from(
            userStorage.getUser(Username(username))
                ?: throw DomainException("User not found", NOT_FOUND)
        )
    }

    companion object {
        private val log = LoggerFactory.getLogger(UserController::class.java)
    }
}

data class UserResponse(
    val id: String,
    val username: String,
    val status: String,
) {
    companion object {
        fun from(user: UserEntity): UserResponse {
            return UserResponse(
                id = user.id.toString(),
                username = user.username,
                status = user.status,
            )
        }
    }
}
