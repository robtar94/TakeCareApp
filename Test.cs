using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using OpenQA.Selenium;
using OpenQA.Selenium.Firefox;
namespace Aplikacja
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Rozpoczynam test logowania!");

            var binary = new FirefoxBinary(@"C:\software\firefox\firefox.exe");
            var profile = new FirefoxProfile();
            FirefoxDriverService service = FirefoxDriverService.CreateDefaultService(@"J:\Desktop\Automaty");
            service.FirefoxBinaryPath = @"C:\software\firefox\firefox.exe";
            var driver = new FirefoxDriver();

            var url = "http://127.0.0.1:5173/#!/login";
            driver.Manage().Window.Maximize();
            driver.Navigate().GoToUrl(url);

            string loginX="//input[@id='login']";
            string hasloX = "//input[@id='pass']";
            string zalogujX = "//button[@id='loginBtn']";

            var login = driver.FindElement(By.XPath(loginX));
            var pass = driver.FindElement(By.XPath(hasloX));
            var loguj = driver.FindElement(By.XPath(zalogujX));

            string log = "student";
            string pas = "student";

            login.Click();
            login.Clear();
            login.SendKeys(log);
            pass.Click();
            pass.Clear();
            pass.SendKeys(pas);
            loguj.Click();

            //driver.Close();

            Console.WriteLine("Test logowania zakończony powodzeniem!");




        }
    }
}
