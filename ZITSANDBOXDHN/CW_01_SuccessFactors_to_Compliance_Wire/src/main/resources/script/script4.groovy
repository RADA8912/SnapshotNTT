package script

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import java.text.SimpleDateFormat

Message processData(Message message) {
/*
    //Accessmessagebodyandproperties
    Reader reader = message.getBody(Reader)
    Map properties = message.getProperties()

    //DefineXMLparserandbuilder
    def queryCompoundEmployeeResponse = new XmlSlurper().parse(reader)
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)

    //DefineVarforEndDateLogic
    def calCurrent = Calendar.getInstance()
    def sdf = new SimpleDateFormat("yyyy-MM-dd")
    def Currentday = calCurrent.get(Calendar.DAY_OF_YEAR)
    def CurrentYear = calCurrent.get(Calendar.YEAR)

    def validCompany = 0
    def hasEndDate = 0
    def dateValid = 0
    def yearErg = 0

    //Definetargetpayloadmapping
    builder.queryCompoundEmployeeResponse {
        //TEST 12.10.2022
        //DefineForEachCompoundEmployee
        def validCompound = queryCompoundEmployeeResponse.CompoundEmployee.findAll {
            compound ->

                //Conditions CreateCompoundEmployee
                //Filter CompanyCodes
                for (def counter = 0; counter < compound.person.employment_information.job_information.company.size(); counter++) {
                    validCompany = compound.person.employment_information.job_information.company[counter] == '1000' ||
                            compound.person.employment_information.job_information.company[counter] == '3010' ||
                            compound.person.employment_information.job_information.company[counter] == '3020' ||
                            compound.person.employment_information.job_information.company[counter] == '3030' ||
                            compound.person.employment_information.job_information.company[counter] == '3040' ||
                            compound.person.employment_information.job_information.company[counter] == '3050' ||
                            compound.person.employment_information.job_information.company[counter] == '3060' ||
                            compound.person.employment_information.job_information.company[counter] == '3070' ||
                            compound.person.employment_information.job_information.company[counter] == '3080' ||
                            compound.person.employment_information.job_information.company[counter] == '3090' ||
                            compound.person.employment_information.job_information.company[counter] == '3100' ||
                            compound.person.employment_information.job_information.company[counter] == '3110' ||
                            compound.person.employment_information.job_information.company[counter] == '3120' ||
                            compound.person.employment_information.job_information.company[counter] == '5400' ||
                            compound.person.employment_information.job_information.company[counter] == '5500'

                    if (compound.person.employment_information.end_date[counter].isEmpty() == true && validCompany == true) {
                        hasEndDate = 'ok'
                    } else hasEndDate = false

                    def enddates = compound.person.employment_information.end_date.text()
                    if (compound.person.employment_information.end_date[counter] != '') {
                        def cal = sdf.parse(compound.person.employment_information.end_date[counter].toString())
                        def calendDate = cal.toCalendar()
                        def day = calendDate.get(Calendar.DAY_OF_YEAR)
                        def year = calendDate.get(Calendar.YEAR)
                        def dayErg = day - Currentday
                        yearErg = CurrentYear - year

                        if (yearErg == 0) {
                            dateValid = dayErg
                        } else if (yearErg < 0) {
                            dateValid = 0
                        } else dateValid = -10
                    } else dateValid = 0

                    dateValid = dateValid >= -7 && validCompany == true

                    if (dateValid == true) {
                        compound.person.created_by.replaceBody(true)
                        break
                    } else compound.person.created_by.replaceBody(false)
                }

                // Filter
                compound.person.person_id_external.text().isNumber() == true &&
                        compound.person.employment_information.job_information.text() != '' &&
                        compound.person.created_by == true &&
                        compound.log.text().isEmpty() == true
        }

        //IterateeachCompoundEmployee
        validCompound.each {
            compound ->
                'CompoundEmployee' {
                    'person'() {
                        'date_of_birth'(compound.person.date_of_birth)
                        'logon_user_name'(compound.person.logon_user_name)
                        'person_id_external'(compound.person.person_id_external)
                        'place_of_birth'(compound.person.place_of_birth)


                        //DefineforEachPersonal_Information
                        def validPersonalInfo = compound.person.personal_information.findAll {
                            personal ->
                                personal != ''
                        }

                        validPersonalInfo.each {
                            personal ->
                                'personal_information' {
                                    'salutation'(personal.salutation)
                                    'title'(personal.title)
                                    'first_name'(personal.first_name)
                                    'last_name'(personal.last_name)
                                    'nationality'(personal.nationality)
                                    'native_preferred_lang'(personal.native_preferred_lang)
                                    'martial_status'(personal.martial_status)

                                    //DefineforEachPersonal_Information_DEU
                                    def validPersonalInfo_deu = personal.personal_information_deu.findAll {
                                        personalDeu ->
                                            personalDeu != ''
                                    }

                                    validPersonalInfo_deu.each {
                                        personalDeu ->
                                            'personal_information_deu' {
                                                'custom_string8'(personalDeu.custom_string8)
                                                'genericString1'(personalDeu.genericString1)
                                            }
                                    }
                                }
                        }

                        //DefineforEachAddress_Information
                        def validAddressInfo = compound.person.address_information.findAll {
                            address ->
                                address != ''
                        }

                        validAddressInfo.each {
                            address ->
                                'address_information' {
                                    'address1'(address.address1)
                                    'address2'(address.address2)
                                    'zip_code'(address.zip_code)
                                    'city'(address.city)
                                    'country'(address.country)
                                }
                        }

                        //DefineforEachphone_information
                        def validPhone = compound.person.phone_information.findAll {
                            phone ->
                                phone.phone_type == 'C' || phone.phone_type == 'B'
                        }

                        validPhone.each {
                            phone ->
                                'phone_information' {
                                    'area_code'(phone.area_code)
                                    'country_code'(phone.country_code)
                                    'extension'(phone.extension)
                                    'phone_number'(phone.phone_number)
                                    'phone_type'(phone.phone_type)
                                }
                        }

                        //DefineforEachEmail_information
                        def validEmail = compound.person.email_information.findAll {
                            email ->
                                email.email_type == 'B'
                        }

                        validEmail.each {
                            email ->
                                'email_information' {
                                    'email_address'(email.email_address)
                                    'email_type'(email.email_type)
                                }
                        }
                        //DefineforEachEmployment_Info
                        def validEmployment = compound.person.employment_information.findAll {

                            employment ->
                                employment.job_information.company[0].toString() == '1000' ||
                                        employment.job_information.company[0] == '3010' ||
                                        employment.job_information.company[0] == '3020' ||
                                        employment.job_information.company[0] == '3030' ||
                                        employment.job_information.company[0] == '3040' ||
                                        employment.job_information.company[0] == '3050' ||
                                        employment.job_information.company[0] == '3060' ||
                                        employment.job_information.company[0] == '3070' ||
                                        employment.job_information.company[0] == '3080' ||
                                        employment.job_information.company[0] == '3090' ||
                                        employment.job_information.company[0] == '3100' ||
                                        employment.job_information.company[0] == '3110' ||
                                        employment.job_information.company[0] == '3120' ||
                                        employment.job_information.company[0] == '5400' ||
                                        employment.job_information.company[0] == '5500'
                        }

                        validEmployment.each {
                            employment ->
                                'employment_information' {
                                    'direct_reports'(employment.direct_reports)
                                    //'end_date'(employment.end_date)
                                    'serviceDate'(employment.serviceDate)
                                    if (employment.SecondaryAssignmentPeriod != '') {
                                        'SecondaryAssignmentPeriod'{
                                            'createdBy'(employment.SecondaryAssignmentPeriod.createdBy)
                                        }
                                    }

                                    if (employment.end_date != '') {
                                        'end_date'(employment.end_date)
                                    }

                                    def validJob = employment.job_information.findAll {
                                        job ->
                                            job != ''
                                    }

                                    validJob.each {
                                        job ->
                                            'job_information' {
                                                if (job.company != '') {
                                                    'company'(job.company)
                                                }

                                                'company_territory_code'(job.company_territory_code)

                                                if (job.cost_center != '') {
                                                    'cost_center'(job.cost_center)
                                                }

                                                'business_unit'(job.business_unit)
                                                'custom_date2'(job.custom_date2)
                                                'custom_string27'(job.custom_string27)
                                                'custom_string4'(job.custom_string4)
                                                'custom_string9'(job.custom_string9)
                                                'department'(job.department)
                                                'division'(job.division)
                                                'emplStatus'(job.emplStatus)
                                                'employee_class'(job.employee_class)
                                                'employment_type'(job.employment_type)
                                                'fte'(job.fte)
                                                'job_code'(job.job_code)
                                                'job_title'(job.job_title)
                                                'location'(job.location)
                                                'manager_id'(job.manager_id)
                                                'manager_person_id_external'(job.manager_person_id_external)
                                                'pay_grade'(job.pay_grade)
                                                'position'(job.position)
                                                'regular_temp'(job.regular_temp)
                                                'standard_hours'(job.standard_hours)
                                                'workingDaysPerWeek'(job.workingDaysPerWeek)
                                            }
                                    }
                                    //DefineforEachCompensation_Information
                                    def validCompensation = employment.compensation_information.findAll {
                                        compensation ->
                                            compensation != ''
                                    }

                                    validCompensation.each {
                                        compensation ->
                                            'compensation_information' {
                                                'pay_type'(compensation.pay_type)

                                                //DefineforEachPersonal_Information_DEU
                                                def validPaycompensationRecurring = compensation.paycompensation_recurring.findAll {
                                                    paycompensation ->
                                                        paycompensation != ''
                                                }

                                                validPaycompensationRecurring.each {
                                                    paycompensation ->
                                                        'paycompensation_recurring' {
                                                            'currency_code'(paycompensation.currency_code)
                                                            'pay_component'(paycompensation.pay_component)
                                                            'paycompvalue'(paycompensation.paycompvalue)

                                                        }
                                                }
                                            }
                                    }
                                    def validCust = employment.cust_concur.findAll {
                                        cust ->
                                            cust != ''
                                    }

                                    validCust.each {
                                        cust ->
                                            'cust_concur' {
                                                'cust_includeExternal'(cust.cust_includeExternal)
                                            }
                                    }
                                }
                        }
                    }
                }
        }
    }

    //Generateoutput
    message.setBody(writer.toString())*/
    return message
}