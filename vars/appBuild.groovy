def call(Map config = [:]) {
    stage('Build') {
        echo "Building application: ${config.appName}"
        if (config.customBuild) {
            // Application-specific override hook
            config.customBuild()
        } else {
            // Default containerized Maven/Gradle/NPM build
            sh './mvnw clean package -DskipTests=false'
        }
    }
}