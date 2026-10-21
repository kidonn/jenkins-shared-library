def call(Map config = [:]) {
    stage('Deploy') {
        echo "Executing ${config.deploymentStrategy} deployment for ${config.appName} to ${config.environment}"
        
        switch (config.deploymentStrategy) {
            case 'canary':
                sh "echo 'Deploying canary pods to ${config.environment}...'"
                break
            case 'blue-green':
                sh "echo 'Switching active blue/green target in ${config.environment}...'"
                break
            case 'rolling':
            default:
                sh "echo 'Executing rolling rollout in ${config.environment}...'"
                break
        }
    }
}