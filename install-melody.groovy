properties([
    parameters([
        string(defaultValue: '', description: 'Provide node IP', name: 'NODE', trim: true)
        ])
    ])

node {
    stage("Pull repo"){
        sh "rm -rf ansible-melodi && git clone https://github.com/ikambarov/ansible-melodi.git"
    }
    stage("Install Melody"){
        ansiblePlaybook become: true, colorized: true, credentialsId: 'jenkins-master', disableHostKeyChecking: true, inventory: "${params.NODE},", playbook: 'ansible-melodi/main.yml'
    }
}