def call(Map userConfig = [:]) {
    // 1. Merge default configs with user-supplied arguments
    Map config = [
        appName           : 'unnamed-app',
        environment       : 'dev',
        deploymentStrategy: 'rolling',
        customBuild       : null,
        preDeployHook     : null
    ] << userConfig

    pipeline {
        agent any
        parameters {
            string(name: 'APP_NAME', defaultValue: config.appName, description: 'Application identifier')
            choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'production'], description: 'Deployment Target')
            choice(name: 'DEPLOY_STRATEGY', choices: ['rolling', 'canary', 'blue-green'], description: 'Rollout Strategy')
        }
        stages {
            stage('Initialize') {
                steps {
                    script {
                        // Parameter overrides from manual triggers
                        config.environment = params.ENVIRONMENT ?: config.environment
                        config.deploymentStrategy = params.DEPLOY_STRATEGY ?: config.deploymentStrategy
                    }
                }
            }
            stage('Build') {
                steps {
                    appBuild(config)
                }
            }
            stage('Security Compliance') {
                steps {
                    // Mandatory step: cannot be overridden by application teams
                    appScan(config)
                }
            }
            stage('Publish') {
                steps {
                    appPublish(config)
                }
            }
            stage('Pre-Deploy Custom Hook') {
                when { expression { config.preDeployHook != null } }
                steps {
                    script {
                        echo "Executing application custom pre-deploy hook..."
                        config.preDeployHook()
                    }
                }
            }
            stage('Deploy') {
                steps {
                    appDeploy(config)
                }
            }
        }
    }
}