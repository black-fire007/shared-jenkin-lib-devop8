def call(String appType) {

    echo "Deploying using docker-compose for ${appType}"

    def composeContent = libraryResource("${appType}/docker-compose.yml")

    writeFile file: 'docker-compose.yml', text: composeContent

    sh """
        docker compose up -d
    """
}
