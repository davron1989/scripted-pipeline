properties([
    parameters([
        string(defaultValue: '', description: 'Provide node IP', name: 'NODE', trim: true)
        ])
    ])

node {
    stage("Pull repo"){
        sh "git clone https://github.com/ikambarov/ansible-melodi.git"
    }
    withCredentials([sshUserPrivateKey(credentialsId: 'jenkins-master', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSERNAME')]) {
        stage("Install Melody"){
            sh """
                export ANSIBLE_HOST_KEY_CHECKING=False
                ansible-playbook  -i "${params.NODE}," --private-key $SSHKEY  ansible-melodi/main.yml -b -u $SSHUSERNAME
            """
        }
    }
}