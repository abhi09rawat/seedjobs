def coreInfraDeploy(String account, String environment, String jobName) {
    pipelineJob('COREINFRA/Deploy/' + account + '/' + environment + '/' + jobName) {
        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            url('https://github.com/abhi09rawat/seedjobs.git')
                            
                        }
                        branches('main')
                        scriptPath('jenkinsfiles/coreinfra/' + account + '/' + environment + '/' + jobName + '.groovy')
                        extensions {}  // required as otherwise it may try to tag the repo, which you may not want
                    }
                }
            }
        }
    }

    multiJob('COREINFRA/Deploy/' + account + '/' + environment + '/04_eks') {
        label('jenkins-master')
        logRotator {
            numToKeep(10)
        }
        steps {
            phase('base') {
                phaseJob('COREINFRA/Deploy/' + account + '/' + environment + '/eks_base')
            }
            phase('addons') {
                phaseJob('COREINFRA/Deploy/' + account + '/' + environment + '/eks_addons')
            }
        }
    }
}

def coreInfraDeployWithCron(String account, String environment, String jobName, String cronExpression) {
    pipelineJob('COREINFRA/Deploy/' + account + '/' + environment + '/' + jobName) {
        properties {
            pipelineTriggers {
                triggers {
                    cron {
                        spec(cronExpression)
                    }
                }
            }
        }
        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            url('https://github.com/abhi09rawat/seedjobs.git')
                            
                        }
                        branches('main')
                        scriptPath('jenkinsfiles/coreinfra/' + account + '/' + environment + '/' + jobName + '.groovy')
                        extensions {}  // required as otherwise it may try to tag the repo, which you may not want
                    }
                }
            }
        }
    }
}

// String[] accs = ['classic', 'dataplatform', 'devportal', 'main', 'tcs']
String[] accs = ['main']
String[] envs = ['dev', 'tst', 'stg', 'prd']

// classic, dataplatform, devportal, main, tcs
for(String acc in accs) {
    for(String env in envs) {
        coreInfraDeploy(acc, env, 'backend')
        coreInfraDeploy(acc, env, 'iam')
        coreInfraDeploy(acc, env, 'network')
        coreInfraDeploy(acc, env, 'eks_base')
        coreInfraDeploy(acc, env, 'eks_addons')
        coreInfraDeploy(acc, env, 'rds_auth')
    }
}
