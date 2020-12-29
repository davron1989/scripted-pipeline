properties([
    parameters([
        string(defaultValue: '', description: 'Provide node IP', name: 'NODE', trim: true)
        ])
    ])

node{
    stage("Pull repo"){
        git url: 'https://github.com/ikambarov/ansible-Flaskex.git'
    }
    stage("Install Prerequisits"){
        ansiblePlaybook become: true, colorized: true, credentialsId: 'jenkins-master', disableHostKeyChecking: true, inventory: "${params.NODE},", playbook: 'prerequisites.yml'
    }
    withEnv(['FLASKEX_REPO=https://github.com/ikambarov/Flaskex.git', 'FLASKEX_BRANCH=master']) {
        stage("Pull Repo"){
            ansiblePlaybook become: true, colorized: true, credentialsId: 'jenkins-master', disableHostKeyChecking: true, inventory: "${params.NODE},", playbook: 'pull_repo.yml'
        }
    }
    
    stage("Install Python"){
        ansiblePlaybook become: true, colorized: true, credentialsId: 'jenkins-master', disableHostKeyChecking: true, inventory: "${params.NODE},", playbook: 'install_python.yml'
    }
    stage("Start App"){
        ansiblePlaybook become: true, colorized: true, credentialsId: 'jenkins-master', disableHostKeyChecking: true, inventory: "${params.NODE},", playbook: 'start_app.yml'
    }    
}