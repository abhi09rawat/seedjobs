// https://jenkinsci.github.io/job-dsl-plugin/#path/listView

listView('COREINFRA') {
    description('Jobs for COREINFRA')
    jobs {
        regex('COREINFRA.*')
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}
