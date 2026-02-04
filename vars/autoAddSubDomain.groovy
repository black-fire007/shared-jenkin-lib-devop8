def call(String domain, String port) {
    echo "Deploying domain ${domain} for service port ${port}"

    sh """
        chmod +x ${libraryResource('scripts/autoAddSubDomain.sh')}
        sudo ${libraryResource('scripts/autoAddSubDomain.sh')} ${domain} ${port}
    """
}
