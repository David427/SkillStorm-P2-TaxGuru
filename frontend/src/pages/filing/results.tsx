import {
  Form,
  Label,
  Button,
  Fieldset,
  TextInput,
  GridContainer,
  StepIndicator,
  StepIndicatorStep,
} from "@trussworks/react-uswds";
import { FormEvent } from "react";

function Required() {
  return (
    <abbr title="required" className="usa-label--required">
      *
    </abbr>
  );
}

export default function Results() {
  const handlePersonalInfo = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    return;
  };

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h3" ofText="of" stepText="Step">
          <StepIndicatorStep label="Personal Information" />
          <StepIndicatorStep label="W-2 Information" />
          <StepIndicatorStep label="Self Employment" />
          <StepIndicatorStep label="Deductions" />
          <StepIndicatorStep label="Review" />
          <StepIndicatorStep label="Results" />
        </StepIndicator>

        <Form onSubmit={handlePersonalInfo}>
          <Fieldset legend="Get started with an account.">
            <p>
              <abbr title="required" className="usa-hint usa-hint--required">
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
            <TextInput id="password" name="password" type="password" />

            <Button type="submit">Create Account</Button>
          </Fieldset>
        </Form>
      </GridContainer>
    </main>
  );
}
