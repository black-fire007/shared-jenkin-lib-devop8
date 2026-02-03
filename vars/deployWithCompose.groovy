def call(String appType) {

    echo "Deploying using docker-compose for ${appType}"

    // Load docker-compose from shared library
    def composeContent = libraryResource("${appType}/docker-compose.yml")
    writeFile file: 'docker-compose.yml', text: composeContent

    def IMAGE_NAME = "piseth11/reactjs-app"
    def IMAGE_TAG  = env.BUILD_NUMBER
    def IMAGE      = "${IMAGE_NAME}:${IMAGE_TAG}"

    sh """
        docker build -t ${IMAGE} .
        docker push ${IMAGE}

        export IMAGE_NAME=${IMAGE_NAME}
        export IMAGE_TAG=${IMAGE_TAG}

        docker compose down || true
        docker compose up -d
    """
}
