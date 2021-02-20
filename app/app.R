library(shiny)
library(shiny.router)
library(magrittr)
library(ggplot2)
library(plotly)
library(DT)
library(shinyjs)
library(leaflet)
library(tidyverse)

#source(".R/home_module.R",encoding="utf-8")
#source(".R/about_module.R",encoding="utf-8")
#source(".R/profil_module.R",encoding="utf-8")

source("routing_module.R",encoding="utf-8")


ui <- fluidPage(
  useShinyjs(),
  tags$head(
    tags$script(src="js.cookie.js"),
    

tags$script('var token = sessionStorage.getItem(\'token\');
     $(document).on("shiny:sessioninitialized",function(event){
     if(token!=null){
         Shiny.onInputChange("token", token);
        Shiny.onInputChange("auth", 1);
     }
});'),
tags$script('Shiny.addCustomMessageHandler("tokenHandler",     
    function(token) {
     sessionStorage.setItem(\'token\', token);
     Shiny.onInputChange("token", token);
     
    }
);'),
tags$script('Shiny.addCustomMessageHandler("tokenHandlerAccess",     
    function(token) {
    if(token=="#!/profil" || token=="#!/calculator" || token=="#!/iota"){
      let token = sessionStorage.getItem(\'token\');
      if(token==null || token==undefined){
        window.location.replace(\'/#!/home\');
      }}}
);'),
# tags$script('
# 
# $(document).on("shiny:visualchange",function(event){
# Shiny.onInputChange("reload", new Date().getTime());});')
    ),
  uiOutput("logged"),
  
  
  router$ui)



server <- shinyServer(function(input, output, session){
  
  
  barSelected<-function(){
    if(( (session$clientData)$url_hash=="#!/profil")){
      
      
      shinyjs::addCssClass(id="tab4",class = "clicked")
      shinyjs::removeCssClass(id="tab1",class = "clicked")
      shinyjs::removeCssClass(id="tab2",class = "clicked")
      shinyjs::removeCssClass(id="tab3",class = "clicked")
      
      shinyjs::removeCssClass(id="tab5",class = "clicked")
      shinyjs::removeCssClass(id="tab6",class = "clicked")
      shinyjs::removeCssClass(id="tab7",class = "clicked")
      
      
    }else if(( (session$clientData)$url_hash=="#!/login")){
      
      
      shinyjs::addCssClass(id="tab3",class = "clicked")
      
      shinyjs::removeCssClass(id="tab1",class = "clicked")
      shinyjs::removeCssClass(id="tab2",class = "clicked")
      
      shinyjs::removeCssClass(id="tab4",class = "clicked")
      shinyjs::removeCssClass(id="tab5",class = "clicked")
      shinyjs::removeCssClass(id="tab6",class = "clicked")
      shinyjs::removeCssClass(id="tab7",class = "clicked")
      
    }
    
    else if(( (session$clientData)$url_hash=="#!/register")){
      
      
      
      shinyjs::addCssClass(id="tab6",class = "clicked")     
      shinyjs::removeCssClass(id="tab1",class = "clicked")
      shinyjs::removeCssClass(id="tab2",class = "clicked")
      shinyjs::removeCssClass(id="tab3",class = "clicked")
      shinyjs::removeCssClass(id="tab4",class = "clicked")
      shinyjs::removeCssClass(id="tab5",class = "clicked")
      
      shinyjs::removeCssClass(id="tab7",class = "clicked")
      
      
    }else if(( (session$clientData)$url_hash=="#!/calculator")){
      
      
      shinyjs::addCssClass(id="tab5",class = "clicked")      
      shinyjs::removeCssClass(id="tab1",class = "clicked")
      shinyjs::removeCssClass(id="tab2",class = "clicked")
      shinyjs::removeCssClass(id="tab3",class = "clicked")
      shinyjs::removeCssClass(id="tab4",class = "clicked")
      
      shinyjs::removeCssClass(id="tab6",class = "clicked")
      shinyjs::removeCssClass(id="tab7",class = "clicked")
      
      
    }else if(( (session$clientData)$url_hash=="#!/about")){
      
      
      shinyjs::addCssClass(id="tab2",class = "clicked")
      shinyjs::removeCssClass(id="tab1",class = "clicked")
      
      shinyjs::removeCssClass(id="tab3",class = "clicked")
      shinyjs::removeCssClass(id="tab4",class = "clicked")
      shinyjs::removeCssClass(id="tab5",class = "clicked")
      shinyjs::removeCssClass(id="tab6",class = "clicked")
      shinyjs::removeCssClass(id="tab7",class = "clicked")
      
    }else if(( (session$clientData)$url_hash=="#!/firms")){
      
      
      shinyjs::addCssClass(id="tab1",class = "clicked")
      
      shinyjs::removeCssClass(id="tab2",class = "clicked")
      shinyjs::removeCssClass(id="tab3",class = "clicked")
      shinyjs::removeCssClass(id="tab4",class = "clicked")
      shinyjs::removeCssClass(id="tab5",class = "clicked")
      shinyjs::removeCssClass(id="tab6",class = "clicked")
      shinyjs::removeCssClass(id="tab7",class = "clicked")
      
    }
    else if(( (session$clientData)$url_hash=="#!/home" || (session$clientData)$url_hash=="#!/" )){
      
      
      shinyjs::removeCssClass(id="tab1",class = "clicked")
      shinyjs::removeCssClass(id="tab2",class = "clicked")
      shinyjs::removeCssClass(id="tab3",class = "clicked")
      shinyjs::removeCssClass(id="tab4",class = "clicked")
      shinyjs::removeCssClass(id="tab5",class = "clicked")
      shinyjs::removeCssClass(id="tab6",class = "clicked")
      shinyjs::removeCssClass(id="tab7",class = "clicked")
      
    }
  }

  output$logged<-renderUI({
    
    
    if(is.null(input$auth)){
      fluidRow(
        
        inlineCSS(list(.clicked = "background-color: #008375 !important")),
        column(12,
               navbarPage("",
                          tabPanel(a("TakeCareApp",id='takeCareApp',class = "tab-link", href = route_link("/home"))),
                          tabPanel(a("Firmy",id="tab1",class = "tab-link", href = route_link("firms"))),
                          tabPanel(a("O nas",id="tab2",class = "tab-link", href = route_link("about"))),
                          tabPanel(a("Zaloguj",id="tab3",class="tab-link", href = route_link("login"))),
                          tabPanel(a("Rejestracja",id="tab6",class="tab-link", href = route_link("register")))),
                          
               
               
               
        )
      )  
    }else{
      fluidRow(
        
        inlineCSS(list(.clicked = "background-color: #008375 !important")),
        column(12,
               navbarPage("",
                          tabPanel(a("TakeCareApp",id='takeCareApp',class = "tab-link", href = route_link("home"))),
                          tabPanel(a("Firmy",id="tab1",class = "tab-link", href = route_link("firms"))),
                          tabPanel(a("O nas",id="tab2",class = "tab-link", href = route_link("about"))),
                          tabPanel(a("Kalkulator", id ="tab5",class="tab-link", href = route_link("calculator"))),
                          tabPanel(a("Klasyfikator", id ="tab5",class="tab-link", href = route_link("klasyfikator"))),
                          tabPanel(a("Wyloguj",id="tab7",class = "tab-link")),
                          tabPanel(a("Profil",id="tab4",class="tab-link", href = route_link("profil")))),
                          
               
               
               
        )
      )
    }
    
    

    
    
  })
  
  

  
  shinyjs::onclick(id="tab7",expr = {
    
    shinyjs::runjs( 'sessionStorage.removeItem(\'token\');
                      
                      Shiny.onInputChange("token", null);
                      Shiny.onInputChange("auth", null);
                      window.location.replace(\'/#!/login\');')
    
  })
  
  
  observeEvent((session$clientData)$url_hash,{

    barSelected()
    
  })
  
  observeEvent(input$reload,{
    barSelected()
  }

    
    
  )

  
  router$server(input, output, session)
})  

shinyApp(ui=ui,server=server)