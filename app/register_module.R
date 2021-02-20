library(shiny)
library(magrittr)
library(ggplot2)
library(plotly)
library(DT)
library(rjson)

connection <-source("connection_module.R",encoding="utf-8")
registerUI <- function(id) {
  ns <- NS(id)
  fluidPage(
    useShinyjs(),
    tags$head(
      tags$script(src = "message-handler.js"),
      tags$style(HTML("
      @import url('//fonts.googleapis.com/css?family=Lobster|Cabin:400,700');
      @import url('//fonts.googleapis.com/css2?family=Fjalla+One');

      
                    "))),
    
    theme = "style.css",
    
    # App title ----
    
    uiOutput("regform"),
    
    
    fluidRow(
      column(12,
             tags$span("© Copyright Wszystkie prawa zastrzeżone."))%>% tagAppendAttributes(id = 'column-copyright'),
      
    )%>% tagAppendAttributes(id = 'row-footer')
    
    
  )
}
registerServer <- function(input, output,session) {
  

  result <-reactiveValues(name=NULL,surname=NULL,mail=NULL,age=NULL,gender=NULL,login=NULL,pass=NULL,permission=NULL)

  status <-reactiveValues(status=NULL,first=TRUE)
  
  observe({
    if(((session$clientData)$url_hash=="#!/register") & (!is.null(input$token) & length(input$token)>0 )){
      
      
      shinyjs::runjs('window.location.replace(\'/#!/home\');')
    }
  })
  
  
  getStatus <- eventReactive(input$submit, {
      result$name<-input$name
      result$surname<-input$surname
      result$mail<-input$mail
      result$datebirth<-input$age
      result$gender<-input$gender
      result$login<-input$username
      result$password<-input$haslo
      result$permission<-input$permission

      
      reg<-c(grepl("^[A-Z][a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{2,15}$",result$name),
      grepl("^[A-Z][a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{2,20}$",result$surname),
      grepl("^[a-z]+[0-9]*@([a-z]{2,10}\\.)+[a-z]{2,5}$",result$mail),
      grepl("^([a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]+[0-9\\-\\_]*){5,20}$",result$username),
      grepl("^([a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{5,}[0-9]{5,}[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż0-9]*)+$",result$password))
      if(all(reg) & all(result$permission==c(1,2))){
        
        
        status$first = FALSE
        personalData = list(
                            name = result$name,
                            surname = result$surname,
                            email = result$mail,
                            datebirth = result$datebirth,
                            gender = result$gender)
        roleData = list(name="IND")
        
        userData = list(
                        login = result$login,
                        password= result$password,
                        roleDTO= roleData)
        
        to_send = list(
          personalDataDTO = personalData,
                       userDTO = userData)
        r<-httr::POST("http://localhost:8080/api/register",body=to_send,encode = 'json')
        # print(content(r))
        
        if (r$status_code==200){
          status$status = TRUE
        }else{
          status$status = FALSE
        }
      }else{
        
        status$status = FALSE
      }

      
      status$status
      
    
    
  })
  

  output$regform<-renderUI({
    if (status$first==TRUE){
    fluidRow(column(12,
                    wellPanel(
                      textInput("name", label = strong("Imie")),
                      uiOutput("name"),
                      textInput("surname", label = strong("Nazwisko")),
                      uiOutput("surname"),
                      textInput("mail", label = strong("Adres email")),
                      uiOutput("mail"),
                      dateInput("age", label = strong("Data urodzenia"),value = "1970-01-01"),
                      
                      selectInput("gender", label = strong("Plec"),
                                  choices = list("Żenska" = 0, "Meska" = 1), 
                                  selected = 0),
                      
                      textInput("username", label = strong("Login")),
                      uiOutput("username"),
                      passwordInput("haslo", label = strong("Haslo")),
                      uiOutput("haslo"),
                      checkboxGroupInput("permission", label = strong("Wyrażam zgode"),
                                         choices = list("Wyrażam zgode na przetwarzanie moich danych osobowych. *" = 1, "Oswiadczam ze jestem swiadom ze aplikacja TakeCareApp to narzedzie wspierajace diagnostyke i nie moze w pelni zastapic konsultacji medycznej z lekarzem. * " = 2),
                      ),
                      uiOutput("permission"),
                      actionButton("submit","Zarejestruj"),
                      uiOutput("btnResponse",style="color:yellow;")
                      
                    ))%>% tagAppendAttributes(id = 'column-login')
    ) %>% tagAppendAttributes(id = 'row-register')
    }else if(status$status==FALSE){
      
      fluidRow(column(12,
                      wellPanel(
                        textInput("name", label = strong("Imie")),
                        uiOutput("name"),
                        textInput("surname", label = strong("Nazwisko")),
                        uiOutput("surname"),
                        textInput("mail", label = strong("Adres email")),
                        uiOutput("mail"),
                        dateInput("age", label = strong("Data urodzenia"),value = "1970-01-01"),
                        
                        selectInput("gender", label = strong("Plec"),
                                    choices = list("Żenska" = 0, "Meska" = 1), 
                                    selected = 0),
                        
                        textInput("username", label = strong("Login")),
                        uiOutput("username"),
                        passwordInput("haslo", label = strong("Haslo")),
                        uiOutput("haslo"),
                        checkboxGroupInput("permission", label = strong("Wyrażam zgode"),
                                           choices = list("Wyrażam zgode na przetwarzanie moich danych osobowych. *" = 1, "Oswiadczam ze jestem swiadom ze aplikacja TakeCareApp to narzedzie wspierajace diagnostyke i nie moze w pelni zastapic konsultacji medycznej z lekarzem. * " = 2),
                        ),
                        uiOutput("permission"),
                        actionButton("submit","Zarejestruj"),
                        uiOutput("btnResponse",style="color:yellow;")
                        
                      ))%>% tagAppendAttributes(id = 'column-login')
      ) %>% tagAppendAttributes(id = 'row-register')
      
    }else{

      
      
      delay(100, shinyjs::refresh())
    }
    
    
    
  })


  output$btnResponse<-renderUI({
    
    if (getStatus()==TRUE){
          p("OK",style="color:white;text-align:center;")
    }else{
          p("Uzytkownik istnieje lub wprowadzono bledne dane",style="color:yellow;text-align:center;")
    }

      
  })
  
  output$name<-renderUI({
    s<-toString(input$name)
    
    if (s=="" | grepl("^[A-Z][a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{2,15}$",s)==TRUE){
      return()
    }else{
      p("Bład: Imie powinno zaczynac sie od wielkiej litery, zawierac jedynie litery i miec dlugosc od 3 do 15 znaków",style="color:yellow")
        }
    
  })
  
  output$surname<-renderUI({
    s<-toString(input$surname)
    
    if (s=="" | grepl("^[A-Z][a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{2,20}$",s)==TRUE){
      return()
    }else{
      p("Bład: Nazwisko powinno zaczynac sie od wielkiej litery, zawierac jedynie litery i miec dlugosc od 3 do 15 znaków",style="color:yellow")
      
      
    }
    
  })
  
  output$mail<-renderUI({
    s<-toString(input$mail)
    
    if (s=="" | grepl("^[a-z]+[0-9]*@([a-z]{2,10}\\.)+[a-z]{2,5}$",s)==TRUE){
      return()
    }else{
      p("Bład: Mail powinien miec budowe adres@nazwa.domena",style="color:yellow")
      
      
    }
    
  })
  
  output$username<-renderUI({
    s<-toString(input$username)
    if (s==""){
      return()
    }else{
      tmps=s
      number=findLogin(tmps)
      
      if ((grepl("^([a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]+[0-9\\-\\_]*){5,20}$",s)==TRUE) & number==0){
        return()
      }else if(number!=0){
        p("Bład: Login jest zajety",style="color:yellow")
      }else{
        p("Bład: Login powinien skladac si z liter i cyfr i miec dlugosc od 5 do 15 znaków",style="color:yellow")
      }  
    }
    
    
  })
  
  output$haslo<-renderUI({
    s<-toString(input$haslo)
    
    if (s=="" | grepl("^([a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż]{5,}[0-9]{5,}[a-zA-ZĄąĆćĘęŁłŃńÓóŚśŹźŻż0-9]*)+$",s)==TRUE){
      return()
    }else{
      p("Bład: Haslo powinno skladac sie z co najmniej 5 liter i 5 cyfr od 10 znaków",style="color:yellow")
    }
    
  })
  
  output$permission<-renderUI({
    s<-input$permission
    
    if (is.null(s)){
      p("Zaakceptuj warunki korzystania z serwisu",style="color:yellow")
    }else if(all(s==c(1,2))){
      return()
    }else{
      p("Zaakceptuj warunki korzystania z serwisu",style="color:yellow")
    }
    
  })
  
  
}

