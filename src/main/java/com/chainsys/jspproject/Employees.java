package com.chainsys.jspproject;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chainsys.jspproject.commonutil.ExceptionManager;
import com.chainsys.jspproject.commonutil.InvalidInputDataException;
import com.chainsys.jspproject.commonutil.Validator;
import com.chainsys.jspproject.dao.EmployeeDao;
import com.chainsys.jspproject.pojo.Employee;

/**
 * Servlet implementation class Employees
 */
@WebServlet("/Employees")
public class Employees extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletResponse response;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Employees() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<Employee> allEmployee = EmployeeDao.getAllEmployee();
		request.setAttribute("emplist", allEmployee);
		RequestDispatcher rd = request.getRequestDispatcher("/ViewEmp.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("click").equals("ADD EMPLOYEE")) {
			String source = "AddNewEmployee";
			String message = "<h1>Error While " + source + "</h1>";
			PrintWriter out = response.getWriter();
			Employee newemp = new Employee();
			int result = 0;
			int empId = 0;
			String testname = null;
			try {

				String id = request.getParameter("id");
				try {
					Validator.checkStringForParse(id);
				} catch (InvalidInputDataException e) {
					message += " Error in Employee id input </p>";
					String errorPage = ExceptionManager.handleException(e, source, message);
					out.print(errorPage);
					return; // It terminates the Code execution beyond this point
				}
				empId = Integer.parseInt(id);
				try {
					Validator.CheckNumberForGreaterThanZero(empId);
				} catch (InvalidInputDataException e) {
					message += " Error in Employee id input </p>";
					String errorPage = ExceptionManager.handleException(e, source, message);
					out.print(errorPage);
					return;

				}
				newemp.setEmp_ID(empId);
//--------------------------------
				String fname = request.getParameter("fname");
				testname = fname;
				try {
					Validator.checkStringOnly(testname);
				} catch (InvalidInputDataException e) {
					message += " Error in First Name input </p>";
					String errorPage = ExceptionManager.handleException(e, source, message);
					out.print(errorPage);
					return;
				}
				try {
					Validator.checklengthOfString(fname);
				} catch (InvalidInputDataException e) {
					e.printStackTrace();
				}
				newemp.setFirst_name(fname);
//-----------------------------------
				String lname = request.getParameter("lname");
				testname = fname;
				try {
					Validator.checkStringOnly(testname);
				} catch (InvalidInputDataException e) {
					message += " Error in Last Name input </p>";
					String errorPage = ExceptionManager.handleException(e, source, message);
					out.print(errorPage);
					return;
				}
				try {
					Validator.checklengthOfString(lname);
				} catch (InvalidInputDataException e) {
					e.printStackTrace();
				}
				newemp.setLast_name(lname);
//----------------------------------			
				String email = request.getParameter("email");
				try {
					Validator.checkMail(email);
				} catch (InvalidInputDataException e) {
					message += " Error in email input </p>";
					String errorPage = ExceptionManager.handleException(e, source, message);
					out.print(errorPage);
					return;
				}
				newemp.setEmail(email);
//--------------------------------------			
				SimpleDateFormat hire_dateFormate = new SimpleDateFormat("dd/MM/yyyy");
				String emp_HireDate = request.getParameter("hdate");
				// Date hire_date=hire_dateFormate.parse(emp_HireDate);

				try {
					Validator.checkDateFormat(emp_HireDate);
				} catch (InvalidInputDataException e) {
					message += " Error in Hire Date input </p>";
					String errorPage = ExceptionManager.handleException(e, source, message);
					out.print(errorPage);
					return;
				}
				Date newDate = null;
				try {
					newDate = hire_dateFormate.parse(emp_HireDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				try {
					Validator.CheckNofutureDate(newDate);
				} catch (InvalidInputDataException e) {
					e.printStackTrace();
				}

				newemp.setHire_date(newDate);
//----------------------------------------
				String jobId = request.getParameter("jobid");
				try {
					Validator.checkjob(jobId);
				} catch (InvalidInputDataException e) {
					message += " Error in Job Id input </p>";
					String errorPage = ExceptionManager.handleException(e, source, message);
					out.print(errorPage);
					return;
				}
				newemp.setJob_id(jobId);
//---------------------------------------			
				String sal = request.getParameter("salary");
				try {
					Validator.checkStringForParse(sal);
				} catch (InvalidInputDataException e) {
					message += " Error in Salary input </p>";
					String errorPage = ExceptionManager.handleException(e, source, message);
					out.print(errorPage);
					return;
				}
				float salParse = Float.parseFloat(sal);
				try {
					Validator.checkSalLimit(salParse);
				} catch (InvalidInputDataException e) {
					message += " Error while inserting record </p>";
					String errorPage = ExceptionManager.handleException(e, source, message);
					out.print(errorPage);
					return;
				}
				newemp.setSalary(salParse);
//--------------------------------------------------------			
				// result = EmployeeDao.insertEmployee(newemp);
			} catch (Exception e) {
				message += "Message: " + e.getMessage();
				String errorPage = ExceptionManager.handleException(e, source, message);
				out.print(errorPage);
				return;
			}
			result = EmployeeDao.insertEmployee(newemp);
			request.setAttribute("addemp", result);
			RequestDispatcher rd = request.getRequestDispatcher("/AddEmployee.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("click").equals("Update EMPLOYEE")) {
			doPut(request, response);
		} else if (request.getParameter("click").equals("Delete Employee")) {
			doDelete(request, response);
		}

	}

	protected void doPut(HttpServletRequest request, HttpServletResponse Response)
			throws ServletException, IOException {
		PrintWriter out = Response.getWriter();
		String source = "AddNewEmployee";
		String message = "<h1>Error While " + source + "</h1>";
		Employee newemp = new Employee();
		int result = 0;
		int empId = 0;
		String testname = null;
		try {
			String id = request.getParameter("id");
			try {
				Validator.checkStringForParse(id);
			} catch (InvalidInputDataException e) {
				message += " Error in Employee id input </p>";
				String errorPage = ExceptionManager.handleException(e, source, message);
				out.print(errorPage);
				return; // It terminates the Code execution beyond this point
			}

			empId = Integer.parseInt(id);
			try {
				Validator.CheckNumberForGreaterThanZero(empId);
			} catch (InvalidInputDataException e) {
				message += " Error in Employee id input </p>";
				String errorPage = ExceptionManager.handleException(e, source, message);
				out.print(errorPage);
				return;
			}
			newemp.setEmp_ID(empId);
//--------------------------------
			String fname = request.getParameter("fname");
			testname = fname;
			try {
				Validator.checkStringOnly(testname);
			} catch (InvalidInputDataException e) {
				out.println("Error first name:" + e.getMessage());

			}
			try {
				Validator.checklengthOfString(fname);
			} catch (InvalidInputDataException e) {
				out.println("Error first name:" + e.getMessage());
			}
			newemp.setFirst_name(fname);
//-----------------------------------
			String lname = request.getParameter("lname");
			testname = fname;
			try {
				Validator.checkStringOnly(testname);
			} catch (InvalidInputDataException e) {
				out.println("Error in Last name:" + e.getMessage());
				return;
			}
			try {
				Validator.checklengthOfString(lname);
			} catch (InvalidInputDataException e) {
				out.println("Error Last name:" + e.getMessage());
			}
			newemp.setLast_name(lname);
//----------------------------------			
			String email = request.getParameter("email");
			try {
				Validator.checkMail(email);
			} catch (InvalidInputDataException e) {
				out.println("Error in Email:" + e.getMessage());

			}
			newemp.setEmail(email);
//--------------------------------------			
			SimpleDateFormat hire_dateFormate = new SimpleDateFormat("dd/MM/yyyy");
			String emp_HireDate = request.getParameter("date");
			// Date hire_date=hire_dateFormate.parse(emp_HireDate);

			try {
				Validator.checkDateFormat(emp_HireDate);
			} catch (InvalidInputDataException e) {
				out.println("Error in Hire date:" + e.getMessage());
			}
			Date newDate = null;
			try {
				newDate = hire_dateFormate.parse(emp_HireDate);
			} catch (ParseException e) {
				out.println("Error in Hire date:" + e.getMessage());
			}
			try {
				Validator.CheckNofutureDate(newDate);
			} catch (InvalidInputDataException e) {
				out.println("Error in Hire date:" + e.getMessage());
			}

			newemp.setHire_date(newDate);
			String jobId = request.getParameter("jobid");
			try {
				Validator.checkjob(jobId);
			} catch (InvalidInputDataException e) {
				out.println("Error in Job id:" + e.getMessage());
			}
			newemp.setJob_id(jobId);
			String sal = request.getParameter("salary");
			try {
				Validator.checkStringForParse(sal);
			} catch (InvalidInputDataException e) {
				out.println("Error in Salary:" + e.getMessage());
			}
			float salParse = Float.parseFloat(sal);
			try {
				Validator.checkSalLimit(salParse);
			} catch (InvalidInputDataException e) {
				out.println("Error in Salary:" + e.getMessage());
			}
			newemp.setSalary(salParse);
			result = EmployeeDao.updateEmployee(newemp);
		} catch (Exception e) {
			out.println("Error in some input data :" + e.getMessage());
		} finally {
		}
		request.setAttribute("modifyemp", result);
		RequestDispatcher rd = request.getRequestDispatcher("ModifyEmployee.jsp");
		rd.forward(request, response);
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		{
			PrintWriter out = response.getWriter();
			Employee newemp = null;
			try {
				newemp = new Employee();
				String id = request.getParameter("id");
				try {
					Validator.checkStringForParse(id);
				} catch (InvalidInputDataException err) {
					out.println("Error in Id:" + err.getMessage());
				}
				int empId = Integer.parseInt(id);
				try {
					Validator.CheckNumberForGreaterThanZero(empId);
				} catch (InvalidInputDataException err) {
					out.println("Error in Id:" + err.getMessage());
				}
				newemp.setEmp_ID(empId);
				int result = EmployeeDao.deleteEmployee(empId);
				request.setAttribute("modifyemp", result);
				RequestDispatcher rd = request.getRequestDispatcher("Delemp.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}