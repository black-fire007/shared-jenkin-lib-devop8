def call(String domain, String port) {
    echo "Deploying domain ${domain} for service port ${port}"

    def scriptPath = "autoAddSubDomain.sh"

    // 1. Load script from resources
    def scriptContent = libraryResource('scripts/autoAddSubDomain.sh')

    // 2. Write it to workspace
    writeFile file: scriptPath, text: scriptContent

    // 3. Make executable
    sh "chmod +x ${scriptPath}"

    // 4. Run it
    sh "sudo ./${scriptPath} ${domain} ${port}"
}
