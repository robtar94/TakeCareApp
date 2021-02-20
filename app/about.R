library(shiny)
library(magrittr)
library(ggplot2)
library(plotly)
library(DT)

aboutUI <- function(id){
  ns <- NS(id)
  

  fluidPage(
  tags$head(
    tags$link(rel = "stylesheet", type = "text/css", href = "style.css")
  ),
    fluidPage(
      h1("Kim jesteśmy?", align = "center"),
      br(),
      h4("Jesteśmy młodym dynamicznym zespołem wdrażającym się w świat tworzenia profesjonalnych aplikacji"),
      h4("Nasz zespół składa się z:"),
      br(),
      
      fluidRow(column(4,
                      div(div("Jan Przybylski"),img(src="jp.png", height = 150, width = 150)))%>% tagAppendAttributes(class = 'column-tile'),
               column(4,
                          div(div("Rafał Piskorski"),img(src="rafal.png", height = 150, width = 150)))%>% tagAppendAttributes(class = 'column-tile'),
               column(4,
                          div(div("Robert Tarnas"),img(src="robert.png", height = 150, width = 150)))%>% tagAppendAttributes(class = 'column-tile'), 
                                 
                        ),
               
               
      
      
      
      

    )
)
}

aboutServer <- function(input, output, session) {


}
