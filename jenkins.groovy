properties([
    parameters([
	    string(defaultValue: 'master', description: 'Enter Devops git repo branch to be checkout', name: 'gitBranch', trim: true),
        string(defaultValue: '', description: 'Enter Devops git repo url', name: 'gitUrl', trim: true),
        string(defaultValue: '', description: 'Enter JAVA_HOME path for agent', name: 'javaHome', trim: true),
		string(defaultValue: 'pom.xml', description: 'Enter path for pom file', name: 'pomFile', trim: true),
		string(defaultValue: '-Dmaven.test.skip=true -s devopsrepo/settings.xml', description: 'Enter maven options ', name: 'mvnOpts', trim: true),
        string(defaultValue: 'clean install', description: 'Enter maven target', name: 'mvnTargets', trim: true)
		
    ])
])



node('xxxxxxx') {
        
		stage("Checkout Code") {
            echo "Checkout Code========================================"
            Checkout()
        }
		
		stage("Checkout DevOps Repo") {
            echo "Checkout Code from DevOps Repo========================================"
            CheckoutDevopsRepo()
        }
		
       
        stage('Build project') {
			echo "Starting maven build========================================"
             Build()
                
            }
           
        
}

def Checkout() {
    script {
       checkout scm
    }
}

def CheckoutDevopsRepo() {
    script {
       checkout changelog: false, poll: false, scm: [$class: 'GitSCM', branches: [[name: '*/${gitBranch}']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'devopsrepo']], gitTool: 'Default', submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'xxxxxxxxxxx', url: '${gitUrl}']]]
    }
}

def Build() {
    script {
	 sh """#!/bin/bash
          export JAVA_HOME=$javaHome
          mvn -f $pomFile $mvnTargets $mvnOpts
          """   
	}

}

