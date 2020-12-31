properties([
    parameters([
        booleanParam(defaultValue: true, description: 'Do you want to build?', name: 'terraform_apply'), 
        booleanParam(defaultValue: false, description: 'Do you want to destroy?', name: 'terraform_destroy'),
        choice(choices: ['dev', 'qa', 'prod'], description: 'Chose environment', name: 'environment')
    ])
])

def aws_region_var = ''

if (params.environment == 'dev'){
    aws_region_var = 'us-east-1'
}

else if (params.environment == 'qa'){
    aws_region_var = 'us-east-2'
}
else if (params.environment == 'prod'){
    aws_region_var = 'us-east-3'
}
    
node{
    stage("Pull Repo"){
        cleanWs() 
        git branch: 'master', url: 'https://github.com/davron1989/terraform-vpc.git'
    }
    withEnv(['AWS_REGION=us-east-1']){
        withCredentials([usernamePassword(credentialsId: 'aws_jenkins_key', passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID')]) {
            stage("Terraform Init"){
                sh """
                    bash setenv.sh ${params.environment}.tfvars
                    terraform init
                """
            }
            if(params.terraform_apply){
                stage("Terraform Apply"){
                    sh """
                        terraform apply -var-file ${params.environment}.tfvars -auto-approve
                    """
                }
            }
            else if(params.terraform_destroy){
                stage("Terraform Destroy"){
                    sh """
                        terraform destroy -var-file ${params.environment}.tfvars -auto-approve
                    """
                }
            }
            else {
                stage("Terraform Plan"){
                    sh """
                        terraform plan -var-file ${params.environment}.tfvars
                    """
                }
            }           
        }
    }
    
}