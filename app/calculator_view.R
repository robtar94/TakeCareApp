library(shiny)
library(magrittr)
library(ggplot2)
library(plotly)
library(DT)

calculatorViewUI <- function(id){
  ns <- NS(id)
  
  uiOutput("view")
  

}

calculatorViewServer <- function(input, output, session) {
  
  pl <- list(
    emptyTable="Tabela jest pusta",
    sSearch = "Szukaj",
    sInfo="Wyniki od _START_ do _END_ z _TOTAL_ rekordow",
    sZeroRecords="Brak rekordow",
    sEmptyTable="Pusta tabela",
    oPaginate= list(
      sFirst="Pierwsza", sPrevious="Poprzednia",sLast="Ostatnia", sNext="Nastepna"
    ),
    sLengthMenu = "Pokaz _MENU_ rekordow na stronie"
  )
  
  component <- reactive({
    if (is.null(get_query_param()$id)) {
      return(NULL)
    }else{
      return(as.numeric(get_query_param()$id))
    }
        
  })
  
  component2 <- reactive({
    if(length(get_query_param())==1 ){
    queryParam = as.numeric(get_query_param())
    r = httr::GET(paste("http://localhost:8080/api/prediction/get/",as.character(queryParam),sep = ""),add_headers(Authorization=paste("Bearer",input$token,sep=" ")),encode = 'json')
    r

    }
  })
  component3<-eventReactive(input$pageIOTA,{

    if(length(get_query_param())==1 ){
      queryParam = as.numeric(get_query_param())
      r = httr::GET(paste("http://localhost:8080/api/prediction/get/",as.character(queryParam),sep = ""),add_headers(Authorization=paste("Bearer",input$token,sep=" ")),encode = 'json')
      
      r
    }else{
      NULL
    }
  })
  
  observe({
    
    if(get_page()=="iota"){

        shinyjs::runjs('Shiny.onInputChange("pageIOTA", "iota");')



        
        
      }
    
    
    
  })
  
  output$view<-renderUI({
    response <- component3()

    if(is.null(response)){
      
    }else{
      

        r = response

        if(r$status_code==200){
          # print(content(r)$prediction$parameterInts)
          
          
          p=0
          if(as.numeric(content(r)$prediction$parameterInts[[4]]$value)>=50){
            p=50
          }
          
          
          z=-5.3718+0.0354*as.numeric(content(r)$prediction$parameterInts[[1]]$value)+1.6159*as.numeric(content(r)$prediction$parameterInts[[2]]$value)+1.1768*as.numeric(content(r)$prediction$parameterInts[[3]]$value)+0.0697*p+0.9586*as.numeric(content(r)$prediction$parameterInts[[5]]$value)-2.9486*as.numeric(content(r)$prediction$parameterInts[[6]]$value)
          x=seq(by=1,-8,8)
          y=round(1/(1+exp(-x)),3)
          d=data.frame(x,y)


          g=ggplot(data=d,aes(x=x,y=y))+
            geom_line()+
            geom_point(aes(x=z,y=round(1/(1+exp(-z)),3)),color="red",size=4)+
            geom_hline(aes(yintercept=0.1),linetype = "dashed")+
            geom_text(aes(x=6,y=0.15),label="próg złośliwości: 0.1")+
            labs(x="Realność",y="Prognoza")




          fluidPage(
            fluidRow(
              column(12,
                     tags$div("Kalkulator wskaźnika ryzyka nowotworu jajnika (IOTA LR2)") %>% tagAppendAttributes(class="panel-title"),
                     wellPanel(
                       p("Szczegółowy opis algorytmu znajduje się w artykule: Timmerman D, Testa AC, Bourne T, [i in.]. Model regresji logistycznej do rozróżniania łagodnych i złośliwych guzów przydatków przed operacją: wieloośrodkowe badanie przeprowadzone przez International Ovarian Tumor Analysis Group. J Clin Oncol. 2005, 23, 8794-8801."),
                       p("Ogólnie algorytm LR2 przewiduje, że nowotwór jest łagodny, gdy pacjent jest młody, lity składnik zmiany jest mały i występują cienie akustyczne. Możesz to sprawdzić empirycznie za pomocą różnych kombinacji wartości wejściowych."),

                       HTML(paste("Surowa wartość predyktora (im niższa, tym lepiej): ", strong(content(r)$prediction$resultValue))),
                       br(),
                       HTML(content(r)$prediction$resultText),

                       ggplotly(g),
                       disabled(sliderInput("vslider1", strong("Wiek pacjenta:"),min = 14, max = 100, value = content(r)$prediction$parameterInts[[1]]$value)),
                       disabled(selectInput("vselect1",strong("Obecność wodobrzusza:"),choices = list("Nie"=0,"Tak"=1),selected=content(r)$prediction$parameterInts[[2]]$value)),
                       disabled(selectInput("vselect2",strong("Obecność przepływu krwi w projekcji brodawkowatej:"),choices = list("Nie"=0,"Tak"=1),selected=content(r)$prediction$parameterInts[[3]]$value)),
                       disabled(sliderInput("vslider2", strong("Największa średnica elementu stałego (w mm):"),min = 0, max = 200, value =content(r)$prediction$parameterInts[[4]]$value)),
                       disabled(selectInput("vselect3",strong("Nieregularna wewnętrzna ściana torbieli:"),choices = list("Nie"=0,"Tak"=1),selected=content(r)$prediction$parameterInts[[5]]$value)),
                       disabled(selectInput("vselect4",strong("Obecność cieni akustycznych:"),choices = list("Nie"=0,"Tak"=1),selected=content(r)$prediction$parameterInts[[6]]$value)),
                       downloadButton("reportCalView", "Generuj raport")
                     ))%>% tagAppendAttributes(id = 'column-content')

            ) %>% tagAppendAttributes(id = 'row-content'),
            fluidRow(
              column(12,
                     tags$span("© Copyright Wszystkie prawa zastrzeżone."))%>% tagAppendAttributes(id = 'column-copyright'),
            )%>% tagAppendAttributes(id = 'row-footer')

          )

        }else{
          shinyjs::runjs('window.location.replace(\'/#!/home\');')

        }


      }


    }
    
    )
  
  
  output$reportCalView <- downloadHandler(
    
    filename = "raport.pdf",
    content = function(file) {
      
      tempReport <- file.path(tempdir(), "report.Rmd")
      file.copy("report.Rmd", tempReport, overwrite = TRUE)
      
      p=0
      if(as.numeric(input$slider2)>=50){
        p=50
      }
      z=-5.3718+0.0354*as.numeric(input$vslider1)+1.6159*as.numeric(input$vselect1)+1.1768*as.numeric(input$vselect2)+0.0697*p+0.9586*as.numeric(input$vselect3)-2.9486*as.numeric(input$vselect4)
      x=round(1/(1+exp(-z)),3)
      params <- list(n = input$vslider1,k=input$vslider2,l=input$vselect1,m=input$vselect2,p=input$vselect3,r=input$vselect4,z=x)
      
      
      rmarkdown::render(tempReport, output_file = file,
                        params = params,
                        envir = new.env(parent = globalenv())
      )
    }
  )


}

