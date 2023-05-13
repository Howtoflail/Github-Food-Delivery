describe('The Login Page', () => {
  beforeEach(() => {
    // reset and seed the database prior to every test
    //cy.exec('npm run db:reset && npm run db:seed')
    //cy.exec('npm rake db:seed').its('code').should('eq', 0)

    cy.request('POST', 'localhost:8080/users/save', { first_name: "Mesi",
      last_name: "Junior",
      email: "d8@gmail.com",
      address: "Center",
      phone : "+40736756631",
      password: "Coaiemari123"
     })
      .its('body')
      .as('currentUser')
  })

  it('sets auth session when logging in via form submission', function () {
    const { email, password } = this.currentUser

      cy.visit('http://localhost:3000/login')

      cy.get('input[name=email]').type("d8@gmail.com")
  
      cy.get('input[name=password]').type(`Coaiemari123`)
  
      cy.get('button').click()
  
      cy.url().should('include', 'http://localhost:3000/')
      
      cy.get("p").should("contain", "Hi, Mesi")
  })
})