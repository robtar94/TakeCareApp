library(shiny)
library(magrittr)
library(ggplot2)
library(plotly)
library(DT)

firmyUI <- function(id) {
  ns <- NS(id)
  fluidPage(
    tags$head(
      tags$style(HTML("
      @import url('//fonts.googleapis.com/css?family=Lobster|Cabin:400,700');
      @import url('//fonts.googleapis.com/css2?family=Fjalla+One');
  


      
                    "))),
    
    theme = "style.css",
    
    
    # App title ----
    # App title ----
    
    #  h4("Aplikacja wspomagajaca diagnozowanie i monitorowanie stanu zdrowia", align = "center"),
    
    fluidRow(
      column(12, align="center",
             
      )),
    uiOutput("firms"),
    

    fluidRow(
      column(12,
             tags$span("© Copyright Wszystkie prawa zastrzeżone."))%>% tagAppendAttributes(id = 'column-copyright'),
      
    )%>% tagAppendAttributes(id = 'row-footer')
    
    
  )
}

firmyServer <- function(input, output,session) {
  
  observe({
    
    if(get_page()=="firms"){
      run = paste('Shiny.onInputChange("pageFIRMS","',timestamp(),'");',sep="")
      shinyjs::runjs(run)
      
    }})
  
  componentFirms<-eventReactive(input$pageFIRMS,{
    

    r<-httr::GET("http://localhost:8080/api/firms/all",encode = 'json')
    
      r

  })
  
  output$firms<-renderUI({
    r<-componentFirms()
    
      companies = content(r)$companies
      
      
      if(is.null(companies) | length(companies)==0){
        fluidRow(
          column(12,
                 
                 wellPanel(h2("Brak firm do wyświetlenia"))%>% tagAppendAttributes(id = 'no-firms') ))
      }else{
      
      exampleMap1 = leaflet() %>%
        setView(companies[[1]]$companyData$latitude, companies[[1]]$companyData$longitude, zoom = 16) %>%
        addTiles() %>%
        addMarkers(companies[[1]]$companyData$latitude, companies[[1]]$companyData$longitude, popup = "Poznań")
      
      exampleMap2 = leaflet() %>%
        setView(companies[[2]]$companyData$latitude, companies[[2]]$companyData$longitude, zoom = 16) %>%
        addTiles() %>%
        addMarkers(companies[[2]]$companyData$latitude, companies[[2]]$companyData$longitude, popup = "Poznań")
      # 
      exampleMap3 = leaflet() %>%
        setView(companies[[3]]$companyData$latitude, companies[[3]]$companyData$longitude, zoom = 16) %>%
        addTiles() %>%
        addMarkers(companies[[3]]$companyData$latitude, companies[[3]]$companyData$longitude, popup = "Poznań")
      
      fluidRow(
        column(12,
               
               wellPanel(column(2,img(src="gsk.png", height = 150, width = 150)),
                         column(6,
                                h2(companies[[1]]$companyData$name),
                                h3(companies[[1]]$companyData$email),
                                h3(companies[[1]]$companyData$address)),
                         column(4,exampleMap1)),
               wellPanel(column(2,img(src="bayer.png", height = 150, width = 150)),
                         column(6,
                                h2(companies[[2]]$companyData$name),
                                h3(companies[[2]]$companyData$email),
                                h3(companies[[2]]$companyData$address)),
                 column(4,exampleMap2)),
               wellPanel(column(2,img(src="biofarm.png", height = 150, width = 150)),
                         column(6,
                                h2(companies[[3]]$companyData$name),
                                h3(companies[[3]]$companyData$email),
                                h3(companies[[3]]$companyData$address)),
                                  column(4,exampleMap3)),
               
               
               
        )%>% tagAppendAttributes(id = 'column-firms')
        
      ) %>% tagAppendAttributes(id = 'row-firms')
      }
      
      
    # }
    
  })
  
}
