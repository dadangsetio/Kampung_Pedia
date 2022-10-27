package com.kampungpedia.data.network

import com.kampungpedia.data.network.auth.model.AuthResponse
import com.kampungpedia.data.network.profile.model.ProfileResponse
import com.kampungpedia.utils.NetworkResponseCode
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

object ApiRepositories {

    private val API_WORKER: ApiWorker = ApiWorker()
    internal val networkResponseCode = NetworkResponseCode()

    suspend fun login(email: String, password: String): NetworkResponse<AuthResponse> {
        val paramters = Parameters.build {
            append("email", email)
            append("password", password)
        }

        return try {
            val response = API_WORKER.getClient()
                .submitForm(url = API_WORKER.BASE_URL + "v1/login", formParameters = paramters)
            if (response.status.value != 200){
                (NetworkResponse.Error(response.status.value, response.status.description))
            }else{
                (NetworkResponse.Success(response.body()))
            }
        } catch (e: Throwable) {
            (NetworkResponse.Error(networkResponseCode.checkError(e), e.message ?: ""))
        }
    }

    suspend fun signin(token: String): NetworkResponse<AuthResponse>{
        val params = Parameters.build {
            append("token", token)
        }
        return try {
            val response = API_WORKER.getClient()
                .submitForm(url =  API_WORKER.BASE_URL + "v1/loginOrRegister", formParameters = params)
            (NetworkResponse.Success(response.body()))
        }catch (e: Throwable){
            (NetworkResponse.Error(networkResponseCode.checkError(e), e.message ?: ""))
        }
    }

    suspend fun profle(token: String): NetworkResponse<ProfileResponse>{
        return try {
            val response = API_WORKER.getClient()
                .get(urlString = API_WORKER.BASE_URL + "v1/me"){
                    headers {
                        append("Authorization", "Bearer $token")
                    }
                }

            (NetworkResponse.Success(response.body()))
        }catch (e: Throwable){
            (NetworkResponse.Error(networkResponseCode.checkError(e), e.message ?: ""))
        }
    }
//
//    suspend fun getAllPosts(): NetworkResponse<MutableList<PostResponse>> {
//
//        return try {
//            val response: HttpResponse =
//                API_WORKER.getClient().get(API_WORKER.BASE_URL + "/posts")
//            // Return response
//            (NetworkResponse.Success(response.receive()))
//
//        } catch (e: Throwable) {
//            (NetworkResponse.Error(networkResponseCode.checkError(e)))
//        }
//    }
//
//    suspend fun getPostById(idPost: String): NetworkResponse<MutableList<PostResponse>> {
//
//        return try {
//            val response: HttpResponse =
//                API_WORKER.getClient().get(API_WORKER.BASE_URL + "/posts") {
//                    //posts?id=5
//                    parameter("id", idPost)
//                }
//
//            // Return response
//            (NetworkResponse.Success(response.receive()))
//
//        } catch (e: Throwable) {
//            (NetworkResponse.Error(networkResponseCode.checkError(e)))
//        }
//    }
//
//
//    suspend fun createPost(postResponse: PostResponse): NetworkResponse<Boolean> {
//
//        return try {
//            API_WORKER.getClient().post<HttpResponse> {
//                url(API_WORKER.BASE_URL + "/posts")
//                body = postResponse
//            }
//
//            // Return response
//            (NetworkResponse.Success(true))
//        } catch (e: Throwable) {
//            (NetworkResponse.Error(networkResponseCode.checkError(e)))
//        }
//    }
//
//    suspend fun createPostWithParams(
//        bodyPost: String,
//        title: String,
//        userId: Int
//    ): NetworkResponse<Boolean> {
//
//        return try {
//            //Set body request
//            val post =c
//                hashMapOf<String, Any>("body" to bodyPost, "title" to title, "userId" to userId)
//
//            API_WORKER.getClient().post<HttpResponse> {
//                url(API_WORKER.BASE_URL + "/posts")
//                body = post
//            }
//
//            // Return response
//            (NetworkResponse.Success(true))
//        } catch (e: Throwable) {
//            (NetworkResponse.Error(networkResponseCode.checkError(e)))
//        }
//    }
//
//    suspend fun deletePost(id: Int): NetworkResponse<Boolean> {
//
//        return try {
//
//            API_WORKER.getClient().delete<HttpResponse> {
//                url(API_WORKER.BASE_URL + "/posts/$id")
//            }
//
//            // Return response
//            (NetworkResponse.Success(true))
//        } catch (e: Throwable) {
//            (NetworkResponse.Error(networkResponseCode.checkError(e)))
//        }
//    }
//
//    suspend fun editPost(
//        bodyPost: String,
//        id: Int,
//        title: String,
//        userId: Int
//    ): NetworkResponse<Boolean> {
//        //Set body request
//        val post = hashMapOf<String, Any>("body" to bodyPost, "title" to title, "userId" to userId)
//
//        return try {
//
//            API_WORKER.getClient().put<HttpResponse> {
//                url(API_WORKER.BASE_URL + "/posts/$id")
//                body = post
//            }
//
//            // Return response
//            (NetworkResponse.Success(true))
//        } catch (e: Throwable) {
//            (NetworkResponse.Error(networkResponseCode.checkError(e)))
//        }
//    }
}