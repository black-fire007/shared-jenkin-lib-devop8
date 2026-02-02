// def call(Map config = [:]) {

//     def imageName   = config.imageName
//     def imageTag    = config.imageTag ?: 'latest'
//     def dockerUser  = config.dockerUser
//     def dockerCred  = config.dockerCredId

//     if (!imageName || !dockerUser || !dockerCred) {
//         error "Required parameters: imageName, dockerUser, dockerCredId"
//     }

//     stage('Push Docker Image') {
//         withCredentials([usernamePassword(
//             credentialsId: dockerCred,
//             usernameVariable: 'DOCKER_USER',
//             passwordVariable: 'DOCKER_PASS'
//         )]) {
//             sh """
//               echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
//               docker push ${dockerUser}/${imageName}:${imageTag}
//             """
//         }
//     }
// }

def call(String image) {

    if (!image) {
        error "Image name is required"
    }

    withCredentials([
        usernamePassword(
            credentialsId: 'dockerhub-creds',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
        )
    ]) {
        sh """
            echo "\$DOCKER_PASS" | docker login -u "\$DOCKER_USER" --password-stdin
            docker push ${image}
        """
    }
}
