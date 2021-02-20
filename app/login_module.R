library(shiny)
library(magrittr)
library(ggplot2)
library(plotly)
library(DT)
library(httr)

source("connection_module.R",encoding="utf-8")

loginUI <- function(id) {
  ns <- NS(id)
  fluidPage(
    useShinyjs(),
    tags$head(
      tags$script(src="js.cookie.js"),

tags$script('Shiny.addCustomMessageHandler("tokenHandlerAfterLogin",     
    function(token) {
     sessionStorage.setItem(\'token\', token);
     Shiny.onInputChange("token", token);
     Shiny.onInputChange("auth", 1);
    
    window.location.href=\'/#!/profil\';
    }
);'),
      tags$style(HTML("
      @import url('//fonts.googleapis.com/css?family=Lobster|Cabin:400,700');
      @import url('//fonts.googleapis.com/css2?family=Fjalla+One');

      
                    "))),
    
    theme = "style.css",
    # window.location.replace(\'/#!/profil\');
    # App title ----
    
    fluidRow(column(12,
                    wellPanel(
                      textInput("login", label = strong("Login")),
                      passwordInput("pass", label = strong("Haslo")),
                      uiOutput("loginErr"),
                      actionButton('loginBtn',"Zaloguj")
                      
                    ))%>% tagAppendAttributes(id = 'column-login')
    ) %>% tagAppendAttributes(id = 'row-login'),
    
    
    
    fluidRow(
      column(12,
             tags$span("© Copyright Wszystkie prawa zastrzeżone."))%>% tagAppendAttributes(id = 'column-copyright'),
      
    )%>% tagAppendAttributes(id = 'row-footer')
    
    
  )
}
loginServer <- function(input, output,session) {
  
  isCorrect <- eventReactive(input$loginBtn, {
    tmp<-data.frame(login=input$login,pass=input$pass)
    
    to_send = list(login = tmp$login,
                   password = tmp$pass)
    
    r<-httr::POST("http://localhost:8080/api/login",body=to_send,encode = 'json')
    
    if(r$status_code==200){
      
      response<-(content(r))
      
      session$sendCustomMessage(type='tokenHandlerAfterLogin', response$token)
      
      TRUE
    }
    

    else{
      FALSE
    }
    
    
    })
  
  
  output$loginErr<-renderUI({
    if (isCorrect()==TRUE){
      
    }else{
      p("Użytkownik istnieje lub wprowadzono błędne dane",style="color:yellow;text-align:center;")
    }
    
  })
  

  observe({
    
    if((session$clientData)$url_hash=="#!/login"){  
      if(!is.null(input$auth) & length(input$auth)>0 ){
        print("redirect from login page if token is set")
      shinyjs::runjs('window.location.replace(\'/#!/home\');')
      }
      
      
      
    }
  })
  

  
  
  
}

