library(shiny)
library(shiny.router)
library(magrittr)
library(ggplot2)
library(plotly)
library(DT)

source("home_module.R",encoding="utf-8")
source("about.R",encoding="utf-8")
source("profil_module.R",encoding="utf-8")
source("login_module.R",encoding="utf-8")
source("register_module.R",encoding="utf-8")
source("calculator.R", encoding =  "utf-8")
source("calculator_view.R", encoding =  "utf-8")
source("firmy_module.R", encoding = "utf-8")
source("klasyfikator.R", encoding = "utf-8")


home_page <-homeUI(id="home")
about_page <-aboutUI(id="about")
profil_page <-profilUI(id="profil")
login_page <-loginUI(id="login")
register_page <-registerUI(id="register")
calculator_page <- calculatorUI(id="calculator")
calculatorView_page <- calculatorViewUI(id="calculatorView")
firmy_page <- firmyUI(id="firms")
klas_page <- klasyui(id="klasyfikator")

router <- make_router(
  route("home", home_page,homeServer),
  route("about", about_page,aboutServer),
  route("profil", profil_page,profilServer),
  route("login", login_page,loginServer),
  route("register", register_page,registerServer),
  route("calculator", calculator_page, calculatorServer),
  route("iota", calculatorView_page, calculatorViewServer),
  route("firms", firmy_page, firmyServer),
  route("klasyfikator",klas_page,klasyserver)

)


