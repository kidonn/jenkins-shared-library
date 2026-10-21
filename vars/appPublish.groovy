def call(Map config = [:]) {
    stage('Publish Artifacts') {
        echo "Publishing artifacts for ${config.appName} (Commit: ${env.GIT_COMMIT.take(7)})"
        sh "echo 'Pushing image to registry: ${config.appName}:${env.BUILD_NUMBER}'"
    }
}