// def call(Map config = [:]) {

//     def appType   = config.appType           // spring | react
//     def imageName = config.imageName
//     def imageTag  = config.imageTag ?: 'latest'
//     def dockerUser = config.dockerUser

//     if (!appType || !imageName || !dockerUser) {
//         error "Required parameters: appType, imageName, dockerUser"
//     }

//     stage('Prepare Docker Files') {

//         if (!fileExists('Dockerfile') && !fileExists('docker-compose.yml')) {
//             echo "Dockerfile and docker-compose.yml not found. Using shared library resources."

//             def dockerfile = libraryResource "docker/${appType}/Dockerfile"
//             writeFile file: 'Dockerfile', text: dockerfile

//             def composeFile = libraryResource "docker/${appType}/docker-compose.yml"
//             writeFile file: 'docker-compose.yml', text: composeFile
//         } else {
//             echo "Docker files found in repository. Using existing files."
//         }
//     }

//     stage('Build Docker Image') {
//         sh """
//           docker build -t ${dockerUser}/${imageName}:${imageTag} .
//         """
//     }
// }


def call(Map config = [:]) {

    if (!config.appType || !config.image) {
        error "Required parameters: appType, image"
    }

    def appType = config.appType
    def image   = config.image
    def tag     = config.tag ?: "latest"

    echo "Building Docker image"
    echo "App Type : ${appType}"
    echo "Image    : ${image}:${tag}"

    // Load Dockerfile from shared library resources
    def dockerfile = libraryResource("${appType}/Dockerfile")
    writeFile file: 'Dockerfile', text: dockerfile

    //   def nginxConf = libraryResource("${appType}/nginx.conf")
    //     writeFile file: 'nginx.conf', text: nginxConf

    sh """
        ls -l
        docker build -t ${image}:${tag} .
    """
}

