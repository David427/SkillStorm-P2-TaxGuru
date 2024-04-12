import {
  Grid,
  Form,
  Label,
  Button,
  Fieldset,
  TextInput,
  GridContainer,
  Icon,
  IconList,
  IconListItem,
  IconListIcon,
  IconListContent,
  IconListTitle,
} from "@trussworks/react-uswds";
import { FormEvent, useState } from "react";
import { Link } from "react-router-dom";

function Required() {
  return (
    <abbr title="required" className="usa-label--required">
      *
    </abbr>
  );
}

export function SignUp() {
  const [showPassword, setShowPassword] = useState(false);

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      email: e.currentTarget.elements.email.value,
      // @ts-expect-error untyped form elements but we need the values
      password: e.currentTarget.elements.password.value,
      // @ts-expect-error untyped form elements but we need the values
      password_confirm: e.currentTarget.elements.password_confirm.value,
    };

    console.log(formData);
    e.currentTarget.reset();
  };

  return (
    <main className="full-page">
      <div className="bg-base-lightest">
        <GridContainer className="usa-section">
          <Grid row className="margin-x-neg-205 flex-justify-center">
            <Grid
              col={12}
              mobileLg={{ col: 10 }}
              tablet={{ col: 8 }}
              desktop={{ col: 6 }}
              className="padding-x-205 margin-bottom-4"
            >
              {/* Mobile only, tagline displays at the top of the screen */}
              <h1 className="desktop:display-none font-sans-lg margin-bottom-4 tablet:margin-top-neg-3">
                A tagline that explains the benefit of creating an account.
              </h1>

              <div className="bg-white padding-y-3 padding-x-5 border border-base-lighter">
                <h2 className="margin-bottom-0">Create Account</h2>
                <Form onSubmit={handleSubmit}>
                  <Fieldset legend="Get started with an account.">
                    <p>
                      <abbr
                        title="required"
                        className="usa-hint usa-hint--required"
                      >
                        *
                      </abbr>{" "}
                      indicates a required field
                    </p>

                    <Label htmlFor="email">
                      Email Address <Required />
                    </Label>
                    <TextInput id="email" name="email" type="email" required />

                    <Label htmlFor="password">
                      Create Password <Required />
                    </Label>
                    <TextInput
                      id="password"
                      name="password"
                      type={showPassword ? "text" : "password"}
                    />
                    <button
                      title="Toggle Password Visibility"
                      type="button"
                      className="usa-show-password"
                      aria-controls="password password_confirm"
                      onClick={() => setShowPassword((prev) => !prev)}
                    >
                      {showPassword ? "Hide Password" : "Show Password"}
                    </button>

                    <Label htmlFor="password">
                      Confirm Password <Required />
                    </Label>
                    <TextInput
                      id="password_confirm"
                      name="password_confirm"
                      type={showPassword ? "text" : "password"}
                    />

                    <Button type="submit">Create Account</Button>
                  </Fieldset>
                </Form>
              </div>

              <p className="text-center">
                Already have an account?{" "}
                <Link to="/login" className="usa-link">
                  Log in
                </Link>
              </p>
            </Grid>

            {/* Extra information */}
            <Grid
              col={12}
              mobileLg={{ col: 10 }}
              tablet={{ col: 8 }}
              desktop={{ col: 6 }}
              className="padding-x-205"
            >
              <div className="border-top border-base-lighter padding-top-4 desktop:border-0 desktop:padding-top-0">
                <h2 className="display-none desktop:display-block">
                  Get started today and file your federal taxes with ease.
                </h2>

                <div className="usa-prose">
                  <p>
                    We'll walk you through the process to ensure 100% accuracy
                    and a maximum refund.
                  </p>
                  <IconList className="usa-icon-list--size-lg">
                    <IconListItem>
                      <IconListIcon className="text-green">
                        <Icon.AttachMoney />
                      </IconListIcon>
                      <IconListContent>
                        <IconListTitle type="h3">
                          Free Federal Filing
                        </IconListTitle>
                        <p>
                          We believe you should have easy and free access to
                          file your federal taxes. That's why we created a
                          seamless platform to allow you to file at no cost.
                        </p>
                      </IconListContent>
                    </IconListItem>

                    <IconListItem>
                      <IconListIcon className="text-green">
                        <Icon.Shield />
                      </IconListIcon>
                      <IconListContent>
                        <IconListTitle type="h3">
                          Industry Grade Security
                        </IconListTitle>
                        <p>
                          We prioritize your data's safety with top-notch
                          security measures to ensure your information stays
                          confidential and protected from unauthorized access.
                        </p>
                      </IconListContent>
                    </IconListItem>
                  </IconList>
                </div>
              </div>
            </Grid>
          </Grid>
        </GridContainer>
      </div>
    </main>
  );
}
