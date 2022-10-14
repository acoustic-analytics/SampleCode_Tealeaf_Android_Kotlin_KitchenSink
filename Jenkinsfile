/**********************************************************************************************
* Copyright (C) 2022 Acoustic, L.P. All rights reserved.
*
* NOTICE: This file contains material that is confidential and proprietary to
* Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
* industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
* Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
* prohibited.
**********************************************************************************************/

def modules = [:]
pipeline {
    agent {
        label 'osx'
    }

    environment {
        SONAR_HOME = "/Users/Shared/Developer/sonar-scanner-4.6.0.2311-macosx/bin"
        SONAR_BUILD_WRAPPER = "/Users/Shared/Developer/build-wrapper-macosx-x86/build-wrapper-macosx-x86"
        PATH="${PATH}:${GEM_HOME}/bin"
    }

    stages {
        stage('Setup') {
            steps {
                echo 'Set up settings..'
            }
        }
        stage('Setup Release') {
            when { branch 'master' }
            steps {
                echo 'Set up settings..'
                script{
                    createBuild("Release ${name} build")
                    checkoutReleaseRepo()
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Building..'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Publish Beta') {
            steps {
                echo 'Publish Beta....'
            }
        }
        stage('Publish Release') {
            when { branch 'master' }
            steps {
                echo 'Publish Release....'
                script{
                    if (genBuild) {
                        echo "Build release"
                        publishRelease()
                    }
                }
            }
        }
    }
    post {
        always {
            script{
                getSlackReport(false)
            }
        }
        // Clean after build
        success {
            cleanWs cleanWhenNotBuilt: false, cleanWhenFailure: false, cleanWhenUnstable: false, deleteDirs: true, disableDeferredWipeout: true, patterns: [[pattern: "**/Reports/**", type: 'EXCLUDE']]
        }
        aborted {
            cleanWs cleanWhenNotBuilt: false, cleanWhenFailure: false, cleanWhenUnstable: false, deleteDirs: true, disableDeferredWipeout: true, patterns: [[pattern: "**/Reports/**", type: 'EXCLUDE']]
        }
    }
}

import groovy.transform.Field
import groovy.json.JsonOutput
import java.util.Optional
import hudson.tasks.test.AbstractTestResultAction
import hudson.model.Actionable
import hudson.tasks.junit.CaseResult
import hudson.model.Action
import hudson.model.AbstractBuild
import hudson.model.HealthReport
import hudson.model.HealthReportingAction
import hudson.model.Result
import hudson.model.Run
import hudson.plugins.cobertura.*
import hudson.plugins.cobertura.targets.CoverageMetric
import hudson.plugins.cobertura.targets.CoverageTarget
import hudson.plugins.cobertura.targets.CoverageResult
import hudson.util.DescribableList
import hudson.util.Graph
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.util.slurpersupport.*
import java.text.SimpleDateFormat

// Global variables
@Field def name              = "Tealeaf_Android_Kotlin_KitchenSink"

// Slack reporting
@Field def gitAuthor         = ""
@Field def lastCommitMessage = ""
@Field def testSummary       = "No tests found"
@Field def coverageSummary   = "No test coverage found"
@Field def lintSummary       = "Lint report is null"
@Field def total             = 0
@Field def failed            = 0
@Field def skipped           = 0

// Version stuff
@Field def currentVersion   = "1.0.0"
@Field def srcBranch        = "main"//"${env.GIT_BRANCH}"

// Commit stuff
@Field def commitDesciption = ""

// Directory paths
@Field def tempTestDir = "${name}Build"
@Field def buildDir    = "${tempTestDir}/Tealeaf_Android_Kotlin_KitchenSink"
@Field def releaseDir  = "${tempTestDir}/SampleCode_Tealeaf_Android_Kotlin_KitchenSink"
// @Field def buildIosDir = "${testAppDir}/ios/derived"

// Build information
@Field def genBuild  = true

def createBuild(findText) {
    def resullt = hasTextBasedOnLastCommit(findText)

    if (resullt == 0) {
        genBuild = false
    } else {
        genBuild = true
    }
}

def hasTextBasedOnLastCommit(findText) {
    def resullt
    
    script {
        resullt = sh (script:'''git log -1 | grep -c \"''' + findText + '''\"
              ''', returnStatus: true)
    }
    return resullt
}

def checkoutReleaseRepo() {
    // Setup temp directory for repos for publishing
    echo "Create test push location: ${tempTestDir}"
    cleanMkDir("${tempTestDir}")
    runCMD("cd ${tempTestDir} && git clone git@github.com:aipoweredmarketer/Tealeaf_Android_Kotlin_KitchenSink.git -b master")
    runCMD("cd ${tempTestDir} && git clone git@github.com:acoustic-analytics/SampleCode_Tealeaf_Android_Kotlin_KitchenSink.git -b main")
}

def runCMD(commnd) {
    OUUUTTPT = sh (
        script: "#!/bin/bash -l\n ${commnd}",
        returnStdout: true
    ).trim()
    echo "${OUUUTTPT}"
    return OUUUTTPT
}

def cleanMkDir(cmDir) {
    removeDir(cmDir)
    runCMD("mkdir -p ${cmDir}")
}

def removeDir(cmDir) {
    def exists = fileExists "${cmDir}"
    if (exists) {
        runCMD("rm -rf ${cmDir}")
    }
}

def publishRelease() {
    echo "Clean up directory in public repo"
    echo "cd ${releaseDir} && git rm -r ."
    runCMD("cd ${releaseDir} && git rm -r .")
    
    echo "Copy over changes from beta to public repo"
    echo "rsync -av --exclude='.git' ${buildDir}/ ${releaseDir}"
    runCMD("rsync -av --exclude='.git' ${buildDir}/ ${releaseDir}")

    updateDescription()
    def commitMsg = "Release ${name} build: ${currentVersion}"
    echo "push with:"
    echo commitMsg
    echo currentVersion
    echo commitDesciption

    // push repos
    gitPush("${releaseDir}", commitMsg, currentVersion, srcBranch, commitDesciption)
}

def updateDescription() {
    def commitDesciptionTitle = "Release ${name} Change Notes:"
    commitDesciption = readFile "latestChanges"
    commitDesciption = "${commitDesciptionTitle} \n" << commitDesciption
    commitDesciption = commitDesciption.replaceAll("\"", "\'")
}

def gitPush(path, commitMsg, tagMsg, branch, commitMsg2) {
    echo "Git Push for: ${path}"
    runCMD('''cd \"''' + path + '''\" && git add . -A''')
    runCMD('''cd \"''' + path + '''\" && git commit -a -m \"''' + commitMsg + '''\" -m \"''' + commitMsg2 + '''\"''')

    // Tag repos
    echo "Tag repos"
    runCMD('''cd \"''' + path + '''\" && git tag -f \"''' + tagMsg + '''\" -m \"''' + commitMsg2 + '''\"''')

    // Pull from git
    echo "Pull from git"
    runCMD('''cd \"''' + path + '''\" && git pull --rebase origin \"''' + branch + '''\"''')

    // Push to git
    echo "Push to git"
    runCMD('''cd \"''' + path + '''\" && git push -f --tags''')
    runCMD('''cd \"''' + path + '''\" && git push -f --set-upstream origin \"''' + branch + '''\"''')
}

def populateSlackMessageGlobalVariables() {
    getLastCommitMessage()
    getGitAuthor()
}

def getGitAuthor() {
    def commit = sh(returnStdout: true, script: 'git rev-parse HEAD')
    gitAuthor = sh(returnStdout: true, script: "git --no-pager show -s --format='%an' ${commit}").trim()
}

def getLastCommitMessage() {
    lastCommitMessage = sh(returnStdout: true, script: 'git log -1 --pretty=%B').trim()
}

def getSlackReport(isRelease) {
    populateSlackMessageGlobalVariables()

    def releaseTitle = ""
    if (isRelease) {
        releaseTitle = "********************Release********************\n"
    }

    echo "currentBuild.result:${currentBuild.result}"

    def buildColor  = "good"
    def buildStatus = currentBuild.result == null ? "Success" : currentBuild.result
    def jobName     = "${env.JOB_NAME}"

    // Strip the branch name out of the job name (ex: "Job Name/branch1" -> "Job Name")
    // echo "job name::;${jobName}"
    jobName = jobName.getAt(0..(jobName.lastIndexOf('/') - 1))

    if (failed > 0) {
        buildStatus = "Failed"
        buildColor  = "danger"
        def failedTestsString = getFailedTests()
    
        notifySlack([
            [
                title: "${jobName}, build #${env.BUILD_NUMBER}",
                title_link: "${env.BUILD_URL}",
                color: "${buildColor}",
                author_name: "${gitAuthor}",
                text: "${releaseTitle}${buildStatus}",
                fields: [
                    [
                        title: "Repo",
                        value: "${name}",
                        short: true
                    ],
                    [
                        title: "Branch",
                        value: "${env.GIT_BRANCH}",
                        short: true
                    ],
                    [
                        title: "Test Results",
                        value: "${testSummary}",
                        short: true
                    ],
                    [
                        title: "Code Coverage Results",
                        value: "${coverageSummary}",
                        short: true
                    ],
                    [
                        title: "Lint Results",
                        value: "${lintSummary}",
                        short: true
                    ],
                    [
                        title: "Last Commit",
                        value: "${lastCommitMessage}",
                        short: false
                    ]
                ]
            ],
            [
                title: "Failed Tests",
                color: "${buildColor}",
                text: "${failedTestsString}",
                "mrkdwn_in": ["text"],
            ]
        ], buildColor)          
    } else {
        notifySlack([
            [
                title: "${jobName}, build #${env.BUILD_NUMBER}",
                title_link: "${env.BUILD_URL}",
                color: "${buildColor}",
                author_name: "${gitAuthor}",
                text: "${releaseTitle}${buildStatus}",
                fields: [
                    [
                        title: "Repo",
                        value: "${name}",
                        short: true
                    ],
                    [
                        title: "Branch",
                        value: "${env.GIT_BRANCH}",
                        short: true
                    ],
                    [
                        title: "Test Results",
                        value: "${testSummary}",
                        short: true
                    ],
                    [
                        title: "Code Coverage Results",
                        value: "${coverageSummary}",
                        short: true
                    ],
                    [
                        title: "Lint Results",
                        value: "${lintSummary}",
                        short: true
                    ],
                    [
                        title: "Last Commit",
                        value: "${lastCommitMessage}",
                        short: false
                    ]
                ]
            ]
        ], buildColor)
    }
}

def notifySlack(attachments, buildColor) {    
    slackSend attachments: attachments, color: buildColor, channel: '#tl-sdk-github'
    slackSend attachments: attachments, color: buildColor, channel: '#acoustic-tealeaf-metova'
    slackSend attachments: attachments, color: buildColor, channel: '#tl-sdk-ci-android-bender'
}

return this

