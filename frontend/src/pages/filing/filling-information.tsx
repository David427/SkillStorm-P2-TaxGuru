import {
  Form,
  Grid,
  Label,
  Radio,
  Select,
  Fieldset,
  TextInput,
  GridContainer,
  StepIndicator,
  StepIndicatorStep,
  ButtonGroup,
  Button,
} from "@trussworks/react-uswds";
import { FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";

export default function FilingInformation() {
  const navigate = useNavigate();

  const handleFilingInfo = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    // @ts-expect-error untyped form elements but we need the values
    const isDependent = e.currentTarget.elements.dependent.value === "Yes";

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      filingStatus: e.currentTarget.elements.filing_status.value,
      // @ts-expect-error untyped form elements but we need the values
      dependents: e.currentTarget.elements.dependents.value,
      isDependent,
    };

    console.log(formData);
    return;

    navigate("/filing/w2");
  };

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h3" ofText="of" stepText="Step">
          <StepIndicatorStep label="Personal Information" status="complete" />
          <StepIndicatorStep label="Filing Information" status="current" />
          <StepIndicatorStep label="W-2 Information" />
          <StepIndicatorStep label="Self Employment" />
          <StepIndicatorStep label="Credits & Deductions" />
          <StepIndicatorStep label="Review" />
          <StepIndicatorStep label="Results" />
        </StepIndicator>

        <Form onSubmit={handleFilingInfo} className="w-full">
          <Fieldset legend="We need a bit more information about your filing status">
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="filing_status" requiredMarker>
                  Filing Status
                </Label>
                <Select id="filing_status" name="filing_status" required>
                  <option>- Select -</option>
                  <option value="single">Single</option>
                  <option value="married_filing_jointly">
                    Married filing jointly
                  </option>
                  <option value="married_filing_separately">
                    Married filing separately
                  </option>
                  <option value="head_of_household">Head of household</option>
                </Select>
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="dependents">
                  How many dependents can you claim?
                </Label>
                <TextInput
                  id="dependents"
                  name="dependents"
                  type="number"
                  defaultValue={0}
                />
              </Grid>
            </Grid>

            <Grid row>
              <Grid tablet={{ col: true }}>
                <p>Can someone claim you as a dependent?</p>
                <Radio
                  id="dependent-yes"
                  name="dependent"
                  value="Yes"
                  label="Yes"
                />
                <Radio
                  id="dependent-no"
                  name="dependent"
                  value="No"
                  label="No"
                />
              </Grid>
            </Grid>

            <div className="tablet:display-flex tablet:flex-justify">
              <ButtonGroup type="default">
                <Link
                  to="/filing/personal"
                  className="usa-button usa-button--outline"
                >
                  Back
                </Link>
                <Button type="submit">Continue</Button>
              </ButtonGroup>
            </div>
          </Fieldset>
        </Form>
      </GridContainer>
    </main>
  );
}
